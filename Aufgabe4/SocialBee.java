import java.util.Iterator;

public interface SocialBee extends Bee{

    /**
     * @return An iterator over every observation of the same individual
     * that indicates a social lifestyle.
     * (Returns all observations if the species is *exclusively* social).
     */
    public Iterator<SocialBee> social();
}
