package sasf.base.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Mapper {

    public <S, D> D mapObject(S source, Class<D> destinationClass) {
        try {
            D destination = destinationClass.getDeclaredConstructor().newInstance();
            copyProperties(source, destination);
            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear objetos", e);
        }
    }

    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            destinationList.add(mapObject(source, destinationClass));
        }
        return destinationList;
    }

    private <S, D> void copyProperties(S source, D destination) throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();
        for (java.lang.reflect.Field sourceField : sourceClass.getDeclaredFields()) {
            try {
                java.lang.reflect.Field destinationField = destinationClass.getDeclaredField(sourceField.getName());
                sourceField.setAccessible(true);
                destinationField.setAccessible(true);
                Object value = sourceField.get(source);
                if (value != null) {
                    destinationField.set(destination, value);
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
    }
}