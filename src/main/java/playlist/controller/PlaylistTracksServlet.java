package playlist.controller;

import playlist.model.PlaylistDAO;
import playlist.model.StatisticsDAO;
import playlist.model.TracksDAO;
import playlist.model.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: stevelowenthal
 * Date: 9/19/13
 * Time: 5:22 PM
 *
 */
public class PlaylistTracksServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession httpSession = request.getSession(true);
    UserDAO user = (UserDAO) httpSession.getAttribute("user");

    // If we're not logged in, go to the login page
    if (user == null) {

      request.setAttribute("error", "Not Logged In");
      response.sendRedirect("login");
      return;
    }

    //
    // Initialize the parameters that are returned from the web page
    //

    String playlist_name = request.getParameter("pl");
    PlaylistDAO playlist = PlaylistDAO.getPlaylistForUser(user, playlist_name);

    String button = request.getParameter("button");
    String deleteTrack = request.getParameter("deleteTrack");

    //
    // If a button was pressed, carry out the button's action
    //

    if (button != null) {
      if (button.contentEquals("addTrack")) {
        String track_id = request.getParameter("track_id");
        doAddPlaylistTrack(playlist, track_id);
      } else if (button.contentEquals("deletePlaylist")) {

        // Clicked to delete the WHOLE playlist
         doDeletePlaylist(playlist);

        // Now go back to the playlists page
         getServletContext().getRequestDispatcher("/playlists").forward(request,response);
        return;
      }

    } else if (deleteTrack != null) {

      // Delete one track
        int sequence_no = Integer.parseInt(deleteTrack);
        doDeleteTrack(playlist, sequence_no);
    }

    request.setAttribute("email", user.getEmail());
    request.setAttribute("playlist", playlist);
    getServletContext().getRequestDispatcher("/playlist_tracks.jsp").forward(request,response);

  }

  void doAddPlaylistTrack(PlaylistDAO playlist, String track_id) throws ServletException {
    // Grab the Track information from the DB
    TracksDAO track = TracksDAO.getTrackById(track_id);

    PlaylistDAO.Track newTrack = new PlaylistDAO.Track(track);
    try {
      playlist.addTracksToPlaylist(Arrays.asList(newTrack));
    } catch (Exception e) {
      throw new ServletException("Couldn't add track to playlist");
    }
  }

  void doDeletePlaylist(PlaylistDAO playlist) {

    StatisticsDAO.decrement_counter("playlists");

    playlist.deletePlayList();
  }

  void doDeleteTrack(PlaylistDAO playlist, int sequence_no) {
      playlist.deleteTrackFromPlaylist(sequence_no);
  }

}
