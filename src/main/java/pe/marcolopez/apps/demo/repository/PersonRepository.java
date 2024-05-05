package pe.marcolopez.apps.demo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import pe.marcolopez.apps.demo.model.entity.PersonEntity;
import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class PersonRepository implements PanacheRepositoryBase<PersonEntity, UUID> {
}
