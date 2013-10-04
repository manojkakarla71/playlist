package playlist.model;

import com.datastax.driver.core.Session;
import junit.framework.TestCase;



/**
 * Created with IntelliJ IDEA.
 * User: stevelowenthal
 * Date: 9/20/13
 * Time: 3:34 PM
 *
 */

public class CassandraDataTest extends TestCase {


  public void testCassandraConnection() {

    // create a session and validate it's not null

    Session session = CassandraData.createSession();
    assertNotNull("session is null",session);

  }

  public void testCassandraSession() throws Exception{

    // Validate that the session is not null
    Session session = CassandraData.getSession();


    assertNotNull("session is null", session);

    // validate we get the same session when we call getSession a second time
    // because getSession is supposed to store the result the first time it's called.

    Session session2 = CassandraData.getSession();

    assertEquals("sessions are not equal", session, session2);


  }


}
