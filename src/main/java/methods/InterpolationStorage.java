package methods;

import java.util.ArrayList;
import java.util.List;

public class InterpolationStorage {
    private final static List<Interpolation> INTERPOLATIONS = new ArrayList<>();

    public static List<Interpolation> getMethods() {
        setMethods();

        return INTERPOLATIONS;
    }

    private static void setMethods() {
        INTERPOLATIONS.add(new LagrangeInterpolation());
        INTERPOLATIONS.add(new NewtonInterpolation());
        INTERPOLATIONS.add(new GaussInterpolation());
    }
}
