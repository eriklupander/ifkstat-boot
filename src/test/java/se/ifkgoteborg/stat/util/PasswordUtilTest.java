package se.ifkgoteborg.stat.util;

import junit.framework.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PasswordUtilTest {

    @Test
    public void testHashPassword() {
        String passwordHash = PasswordUtil.getPasswordHash("5ecret");
        assertEquals("Zwngj0HmTxVUeNuY3Pldgw==", passwordHash);
    }
}
