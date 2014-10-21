package net.philadams.vio;

import java.util.List;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 *
 */
public interface VIOAPIInterface  {

  @GET("/couples/{couple}/thoughts")
  List<Thought> thoughts(@Path("couple") int couple);

  @POST("/couples/{couple}/think/{thinker}")
  Thought think(@Path("couple") int couple, @Path("thinker") String thinker);

}
