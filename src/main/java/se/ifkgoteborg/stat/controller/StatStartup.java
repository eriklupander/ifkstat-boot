package se.ifkgoteborg.stat.controller;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.ifkgoteborg.stat.model.*;
import se.ifkgoteborg.stat.model.enums.MajorVerticalAlignment;
import se.ifkgoteborg.stat.model.enums.MinorHorizontalAlignment;
import se.ifkgoteborg.stat.model.enums.MinorVerticalAlignment;
import se.ifkgoteborg.stat.model.enums.Side;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class StatStartup {

	private static final String INIT_DATA_RUN = "init.data.run";

    @PersistenceContext
	EntityManager em;	
	
	// @PostConstruct   DOES NOT WORK WITH TX IN SPRING!!
    @Transactional
	public void init() {
		
		System.out.println("ENTER - init() of Stat");
		
		boolean runInitData = true;
		
		try {
			Setting s = (Setting) em.createQuery("select s from Setting s WHERE s.key=:key").setParameter("key", INIT_DATA_RUN).getSingleResult();
			if("1".equals(s.getValue())) {
				runInitData = false;
			}
		} catch (NoResultException e) {
			
		}
		
		if(!runInitData)
			return;
	
		// Bootstrap user
//		User user = new User();
//		user.setUsername("admin");
//		// TODO change this once we go live!!!
//		user.setPasswd("ISMvKXpXpadDiUoOSoAfww==");
//
//		user = em.merge(user);
//
//		Userrole userrole = new Userrole();
//		userrole.setUsername("admin");
//		userrole.setUserRoles("admin");
//		userrole = em.merge(userrole);
		
		 createInitData();
	}

    @Transactional
	public void createInitData() {

        // Some Java 8 functional style stuff to create Country instances, unique country names only.
        Arrays.asList(Locale.getAvailableLocales()).stream()
                .filter(locale -> locale.getDisplayName().trim().length() > 0)
                .map(locale -> new Country(locale.getCountry(), locale.getDisplayCountry()))
                .distinct()
                .forEach(country -> em.merge(country));

	    em.flush();

		Country sweden = (Country) em.createQuery("select c from Country c WHERE c.code='SE'").getSingleResult();
		
		Club ifkgbg = new Club();
		ifkgbg.setName("IFK Göteborg");
		ifkgbg.setCity("Göteborg");
		ifkgbg.setCountry(sweden);
		ifkgbg.setDefaultClub(true);
		
		ifkgbg = em.merge(ifkgbg);
		
		Ground gullevi = new Ground();
		gullevi.setName("Gamla Ullevi");
		gullevi.setMaxCapacity(18500);
		
		gullevi = em.merge(gullevi);
		
		PositionType gk = em.merge(new PositionType("Målvakt", MajorVerticalAlignment.GK));
		PositionType def = em.merge(new PositionType("Försvarare", MajorVerticalAlignment.DEF));
		PositionType mid = em.merge(new PositionType("Mittfältare", MajorVerticalAlignment.MF));
		PositionType forw = em.merge(new PositionType("Anfallare", MajorVerticalAlignment.FW));
		
		// Positioner för 4-4-2
		Position mv = em.merge(new Position("Målvakt", "MV", Side.CENTRAL, gk));
		Position hb = em.merge(new Position("Högerback", "HB", Side.RIGHT, def));
		Position mb1 = em.merge(new Position("Mittback", "MB", Side.CENTRAL, def, MinorHorizontalAlignment.RIGHT));
		Position mb2 = em.merge(new Position("Mittback", "MB", Side.CENTRAL, def, MinorHorizontalAlignment.LEFT));
		Position mb1_c = em.merge(new Position("Mittback", "MB", Side.CENTRAL, def, MinorHorizontalAlignment.NEUTRAL));
		Position vb = em.merge(new Position("Vänsterback", "VB", Side.LEFT, def));
		Position hy = em.merge(new Position("Högerytter", "HY", Side.RIGHT, mid));
		Position im1 = em.merge(new Position("Innermitt", "IM", Side.CENTRAL, mid, MinorHorizontalAlignment.RIGHT));
		Position im2 = em.merge(new Position("Innermitt", "IM", Side.CENTRAL, mid, MinorHorizontalAlignment.LEFT));
		Position im1_c = em.merge(new Position("Innermitt", "IM", Side.CENTRAL, mid));
		
		Position im1d_r = em.merge(new Position("Innermitt (def)", "IM (D)", Side.CENTRAL, mid, MinorVerticalAlignment.DEFENSIVE, MinorHorizontalAlignment.RIGHT));
		Position im2d_l = em.merge(new Position("Innermitt (def)", "IM (D)", Side.CENTRAL, mid, MinorVerticalAlignment.DEFENSIVE, MinorHorizontalAlignment.LEFT));
		Position im1d_c = em.merge(new Position("Innermitt (def)", "IM (D)", Side.CENTRAL, mid, MinorVerticalAlignment.DEFENSIVE));
		
		Position im1o = em.merge(new Position("Innermitt (off)", "IM (O)", Side.CENTRAL, mid, MinorVerticalAlignment.OFFENSIVE));
		
		Position vy = em.merge(new Position("Vänsterytter", "VY", Side.LEFT, mid));
		
		Position fw1 = em.merge(new Position("Anfallare", "FW", Side.CENTRAL, forw, MinorHorizontalAlignment.RIGHT));
		Position fw2 = em.merge(new Position("Anfallare", "FW", Side.CENTRAL, forw, MinorHorizontalAlignment.LEFT));
		
		Position fw1_c = em.merge(new Position("Anfallare", "FW", Side.CENTRAL, forw));
		//Position fw1_def = em.merge(new Position("Anfallare", "FW (D)", Side.CENTRAL, forw));
		
		Position p12 = em.merge(new Position("Innermitt", "IM", Side.CENTRAL, mid));
		
		
		Position hh = em.merge(new Position("Högerhalv", "HH", Side.CENTRAL, mid, MinorHorizontalAlignment.RIGHT));
		Position ch = em.merge(new Position("Centerhalv", "CH", Side.CENTRAL, mid));
		Position vh = em.merge(new Position("Vänsterhalv", "VH", Side.CENTRAL, mid, MinorHorizontalAlignment.LEFT));
		
		Position hi = em.merge(new Position("Högerinner", "HI", Side.CENTRAL, forw, MinorHorizontalAlignment.RIGHT));
		Position c = em.merge(new Position("Center", "CE", Side.CENTRAL, forw, MinorVerticalAlignment.OFFENSIVE));
		Position vi = em.merge(new Position("Vänsterinner", "VI", Side.CENTRAL, forw, MinorHorizontalAlignment.LEFT));
			
		// Yttrarna i 2-3-5 
		Position hy_classic = em.merge(new Position("Högerytter", "HY", Side.RIGHT, forw, MinorVerticalAlignment.DEFENSIVE));
		Position vy_classic = em.merge(new Position("Vänsterytter", "VY", Side.LEFT, forw, MinorVerticalAlignment.DEFENSIVE));
		
		Formation f442 = new Formation("4-4-2");
		f442 = em.merge(f442);
		
		Formation f4231 = new Formation("4-2-3-1");
		f4231 = em.merge(f4231);
		
		Formation f4141 = new Formation("4-1-4-1");
		f4141 = em.merge(f4141);
		
		Formation f451 = new Formation("4-5-1");
		f451 = em.merge(f451);
		
		Formation f352 = new Formation("3-5-2");
		f352 = em.merge(f352);
		
		Formation f235 = new Formation("2-3-5");
		f235 = em.merge(f235);
		
		Formation f4411 = new Formation("4-4-1-1");
		f4411 = em.merge(f4411);
		
		Formation f4132 = new Formation("4-1-3-2");
		f4132 = em.merge(f4132);
		
		em.persist(new FormationPosition(1, f442, mv));
		em.persist(new FormationPosition(2, f442, hb));
		em.persist(new FormationPosition(3, f442, mb1));
		em.persist(new FormationPosition(4, f442, mb2));
		em.persist(new FormationPosition(5, f442, vb));
		em.persist(new FormationPosition(6, f442, hy));
		em.persist(new FormationPosition(7, f442, im1));
		em.persist(new FormationPosition(8, f442, im2));
		em.persist(new FormationPosition(9, f442, vy));
		em.persist(new FormationPosition(10, f442, fw1));
		em.persist(new FormationPosition(11, f442, fw2));
		
		em.persist(new FormationPosition(1, f4231, mv));
		em.persist(new FormationPosition(2, f4231, hb));
		em.persist(new FormationPosition(3, f4231, mb1));
		em.persist(new FormationPosition(4, f4231, mb2));
		em.persist(new FormationPosition(5, f4231, vb));
		em.persist(new FormationPosition(6, f4231, im1d_r));
		em.persist(new FormationPosition(7, f4231, im2d_l));
		em.persist(new FormationPosition(8, f4231, hy));
		em.persist(new FormationPosition(9, f4231, im1o));
		em.persist(new FormationPosition(10, f4231, vy));
		em.persist(new FormationPosition(11, f4231, fw1_c));
		
		em.persist(new FormationPosition(1, f4141, mv));
		em.persist(new FormationPosition(2, f4141, hb));
		em.persist(new FormationPosition(3, f4141, mb1));
		em.persist(new FormationPosition(4, f4141, mb2));
		em.persist(new FormationPosition(5, f4141, vb));
		em.persist(new FormationPosition(6, f4141, im1d_c));
		em.persist(new FormationPosition(7, f4141, hy));
		em.persist(new FormationPosition(8, f4141, im1));
		em.persist(new FormationPosition(9, f4141, vy));
		em.persist(new FormationPosition(10, f4141, im2));
		em.persist(new FormationPosition(11, f4141, fw1_c));
		
		em.persist(new FormationPosition(1, f451, mv));
		em.persist(new FormationPosition(2, f451, hb));
		em.persist(new FormationPosition(3, f451, mb1));
		em.persist(new FormationPosition(4, f451, mb2));
		em.persist(new FormationPosition(5, f451, vb));
		em.persist(new FormationPosition(6, f451, hy));
		em.persist(new FormationPosition(7, f451, im1));
		em.persist(new FormationPosition(8, f451, im1_c));
		em.persist(new FormationPosition(9, f451, im2));
		em.persist(new FormationPosition(10, f451, vy));
		em.persist(new FormationPosition(11, f451, fw1_c));
		
		em.persist(new FormationPosition(1, f352, mv));
		em.persist(new FormationPosition(2, f352, hb));
		em.persist(new FormationPosition(3, f352, mb1_c));
		em.persist(new FormationPosition(4, f352, vb));
		em.persist(new FormationPosition(5, f352, hy));
		em.persist(new FormationPosition(6, f352, im1));
		em.persist(new FormationPosition(7, f352, im1_c));
		em.persist(new FormationPosition(8, f352, im2));
		em.persist(new FormationPosition(9, f352, vy));
		em.persist(new FormationPosition(10, f352, fw1));
		em.persist(new FormationPosition(11, f352, fw2));
		

		em.persist(new FormationPosition(1, f235, mv));
		em.persist(new FormationPosition(2, f235, hb));
		em.persist(new FormationPosition(3, f235, vb));
		em.persist(new FormationPosition(4, f235, hh));
		em.persist(new FormationPosition(5, f235, ch));
		em.persist(new FormationPosition(6, f235, vh));
		em.persist(new FormationPosition(7, f235, hy));
		em.persist(new FormationPosition(8, f235, hi));
		em.persist(new FormationPosition(9, f235, c));
		em.persist(new FormationPosition(10, f235, vi));
		em.persist(new FormationPosition(11, f235, vy));
		
		em.persist(new FormationPosition(1, f4411, mv));
		em.persist(new FormationPosition(2, f4411, hb));
		em.persist(new FormationPosition(3, f4411, mb1));
		em.persist(new FormationPosition(4, f4411, mb2));
		em.persist(new FormationPosition(5, f4411, vb));
		em.persist(new FormationPosition(6, f4411, hy));
		em.persist(new FormationPosition(7, f4411, im1));
		em.persist(new FormationPosition(8, f4411, im2));
		em.persist(new FormationPosition(9, f4411, vy));
		em.persist(new FormationPosition(10, f4411, im1o));
		em.persist(new FormationPosition(11, f4411, fw1_c));
		
		
		em.persist(new FormationPosition(1, f4132, mv));
		em.persist(new FormationPosition(2, f4132, hb));
		em.persist(new FormationPosition(3, f4132, mb1));
		em.persist(new FormationPosition(4, f4132, mb2));
		em.persist(new FormationPosition(5, f4132, vb));
		em.persist(new FormationPosition(6, f4132, im1d_c));
		em.persist(new FormationPosition(7, f4132, hy));
		em.persist(new FormationPosition(8, f4132, im1_c));
		em.persist(new FormationPosition(9, f4132, vy));
		em.persist(new FormationPosition(10, f4132, fw1));
		em.persist(new FormationPosition(11, f4132, fw2));
		
		// Write setting flag
		Setting settingsInitialized = new Setting(INIT_DATA_RUN, "1");
		em.persist(settingsInitialized);
		
		System.out.println("FINISHED setting up core data.");
	}
	
}
