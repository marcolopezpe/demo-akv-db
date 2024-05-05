package pe.marcolopez.apps.demo.resource;

import pe.marcolopez.apps.demo.model.entity.PersonEntity;
import pe.marcolopez.apps.demo.repository.PersonRepository;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

  @Inject
  PersonRepository personRepository;

  @GET
  public List<PersonEntity> list() {
    return personRepository.listAll();
  }
}
