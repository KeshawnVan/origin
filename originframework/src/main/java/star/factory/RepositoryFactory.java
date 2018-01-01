package star.factory;

import star.repository.CommonRepository;
import star.repository.RepositoryProxy;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by fankaixiang on 2018/1/1.
 */
public final class RepositoryFactory {

    private static final Map<Class<?>,CommonRepository> REPOSITORY_MAP = buildRepositoryMap();

    private static Map<Class<?>,CommonRepository> buildRepositoryMap(){
        Set<Class<?>> repositoryInterfaces = ClassFactory.getClassSetBySuper(CommonRepository.class);
        return repositoryInterfaces.stream()
                .collect(Collectors.toMap(cls -> cls,cls -> new RepositoryProxy(cls).getProxy()));
    }

    public static Map<Class<?>, CommonRepository> getRepositoryMap() {
        return REPOSITORY_MAP;
    }
}
