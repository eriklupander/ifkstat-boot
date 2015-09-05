package se.ifkgoteborg.stat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.ifkgoteborg.stat.controller.StatStartup;
import se.ifkgoteborg.stat.dao.RegistrationDAO;
import se.ifkgoteborg.stat.importer.controller.MasterImporter;
import se.ifkgoteborg.stat.importer.controller.NotesImporter;
import se.ifkgoteborg.stat.model.User;
import se.ifkgoteborg.stat.model.Userrole;
import se.ifkgoteborg.stat.util.PasswordUtil;
import se.ifkgoteborg.stat.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/rest/superadmin")
public class SuperAdminDataServiceBean {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
    RegistrationDAO registrationDao;

    @Autowired
	MasterImporter masterImporter;

    @Autowired
	NotesImporter notesImporter;

    @Autowired
    StatStartup statStartup;

    @RequestMapping(method = RequestMethod.POST, value = "/user", produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody User user) {
		validateUser(user);
		user.setPasswd(hashPassword(user.getPasswd()));
		user.setFirstName(StringUtil.capitalize(user.getFirstName()));
		user.setLastName(StringUtil.capitalize(user.getLastName()));
		
		Userrole ur = new Userrole();
		ur.setUsername(user.getUsername());
		if(user.getRoles() != null) {			
			ur.setUserRoles(user.getRoles()); // comma-separated string
		} else {
			ur.setUserRoles("user");
		}
		
		user = em.merge(user);
		ur = em.merge(ur);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
		
	}

    @RequestMapping(method = RequestMethod.POST, value = "/init", produces = MediaType.TEXT_PLAIN_VALUE,  consumes = MediaType.TEXT_PLAIN_VALUE)
    @Transactional
    public void execInitData() {
        statStartup.init();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/games", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    @Transactional
    public void bulkUploadGameData(@RequestBody String data) {
		masterImporter.importMasterFile(data);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/notes", consumes = "application/json")
    @Transactional
    public void bulkUploadNotes(@RequestBody String data) {
		notesImporter.importNotes(data);
	}

    @RequestMapping(method = RequestMethod.DELETE, value = "/clean", consumes = "application/json")
    @Transactional
    public void cleanDatabase(@RequestBody String password) {
		registrationDao.cleanDatabase(password);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/initdata", consumes = "application/json")
    @Transactional
    public void reseedInitData(@RequestBody String password) {
		registrationDao.reseedInitData(password);
	}

    private String hashPassword(String passwd) {
        return PasswordUtil.getPasswordHash(passwd);
    }

    private void validateUser(User user) {
        if(isEmpty(user.getUsername())) {
            throw new IllegalArgumentException("Username is required");
        }
        if(isEmpty(user.getPasswd())) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
