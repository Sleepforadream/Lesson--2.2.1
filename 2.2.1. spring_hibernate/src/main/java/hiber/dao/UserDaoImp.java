package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().persist(user);
        sessionFactory.getCurrentSession().persist(user.getCar());
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserByCar(String model, Long series) {
        TypedQuery<Car> queryGetCar = sessionFactory.getCurrentSession().
                createQuery("from Car where model=:model and series = :series", Car.class);
        queryGetCar.setParameter("model", model);
        queryGetCar.setParameter("series", series);
        Car car1 = queryGetCar.getSingleResult();
        TypedQuery<User> getUserQuery = sessionFactory.getCurrentSession().
                createQuery("from User where car.id=:carId", User.class);
        getUserQuery.setParameter("carId", car1.getId());
        return getUserQuery.getSingleResult();
    }
}