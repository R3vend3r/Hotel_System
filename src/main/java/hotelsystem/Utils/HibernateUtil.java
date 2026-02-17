package hotelsystem.Utils;

import hotelsystem.model.entity.Amenity;
import hotelsystem.model.entity.AmenityOrder;
import hotelsystem.model.entity.Room;
import hotelsystem.model.entity.RoomBooking;
import hotelsystem.model.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Amenity.class)
                    .addAnnotatedClass(AmenityOrder.class)
                    .addAnnotatedClass(Room.class)
                    .addAnnotatedClass(RoomBooking.class)
                    .addAnnotatedClass(Client.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
