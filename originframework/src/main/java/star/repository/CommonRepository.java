package star.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public interface CommonRepository<T, ID extends Serializable> {
    T findById(ID id);

    List<T> findAll();

    List<T> findByIdIn(Collection<ID> ids);

    T save(T t);

    T update(T t);

    Boolean delete(ID id);
}
