package se.ifkgoteborg.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import se.ifkgoteborg.stat.filter.CsrfHeaderFilter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
public class Application extends WebMvcConfigurerAdapter {

    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "se.ifkgoteborg.stat.model" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl( env.getProperty("db.url") );
        dataSource.setUsername( env.getProperty("db.username") );
        dataSource.setPassword( env.getProperty("db.password") );
//        dataSource.setUrl("jdbc:mysql://devdatabases.cs0gnntvx6uy.eu-west-1.rds.amazonaws.com/ifkstat");
//        dataSource.setUsername( "ifkstat" );
//        dataSource.setPassword( "g4j5eB4j5" );
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }



    @Configuration
    @EnableWebMvcSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        private SecurityProperties security;

        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and()      //

                    .authorizeRequests()
                    .antMatchers("/rest/admin/**")
                    .hasRole("ADMIN")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/rest/superadmin/**")
                    .hasRole("SUPERADMIN")
                    .and() //.csrf().disable();
                    .formLogin().loginPage("/#/login").permitAll()
                    .and()
                    .logout().logoutSuccessUrl("/#/login?logout").permitAll()
                    .and()
                    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                    .csrf().csrfTokenRepository(csrfTokenRepository());

             // .anyRequest().authenticated()
//            http
//                    .authorizeRequests().anyRequest().authenticated();

//            http
//                    .formLogin().failureUrl("/login?error")
//                    .defaultSuccessUrl("/")
//                    .loginPage("/login")
//                    .permitAll()
//                    .and()
//                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
//                    .permitAll();
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication().withUser("admin").password("admin")
//                    .roles("ADMIN", "USER").and().withUser("user").password("user")
//                    .roles("USER").and().withUser("superadmin").password("superadmin")
//                    .roles("SUPERADMIN", "ADMIN", "USER");

            JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
            userDetailsService.setDataSource(dataSource);
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
            auth.jdbcAuthentication().dataSource(dataSource);

            if(!userDetailsService.userExists("user")) {
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("USER"));
                User userDetails = new User("user", encoder.encode("user"), authorities);

                userDetailsService.createUser(userDetails);
            }
            if(!userDetailsService.userExists("superadmin")) {
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("USER"));
                User userDetails = new User("superadmin", encoder.encode("superadmin"), authorities);

                userDetailsService.createUser(userDetails);
            }
        }
    }

//    @Bean(initMethod = "start", name = "esServer", destroyMethod = "stop")
//    public ElasticSearchServer esServer() {
//        Map<String,String> configuration = new HashMap<>();
//        configuration.put("location","classpath:elasticsearch-server.properties");
//        configuration.put("localOverride", "true");
//      //  configuration.put("path.conf", "${webapp.root}/WEB-INF/config");
//        return new ElasticSearchServer(configuration);
//    }

}
