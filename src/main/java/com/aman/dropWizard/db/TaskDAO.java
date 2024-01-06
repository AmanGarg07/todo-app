package com.aman.dropWizard.db;
import com.aman.dropWizard.core.Task;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {

        public TaskDAO(SessionFactory sessionFactory) {
            super(sessionFactory);
        }

        public Task getById(int id){
            Query<Task> query = currentSession().createNamedQuery("Task.getById", Task.class);
            query.setParameter("id",id);

            return query.getSingleResult();
        }
        public List<Task> getAllTasks() {
            Query<Task> query = currentSession().createNamedQuery("Task.findAll", Task.class);
            return query.getResultList();
        }
        public Task addTask(Task task){

            return persist(task);

        }
        public void delete(Task task){
            Query<Task> query = currentSession().createNamedQuery("Task.deleteById");
            query.setParameter("id",task.getId());
            query.executeUpdate();
        }

        // updates existing task fields with new ones
        public void update(Task task){
            currentSession().update(task);
        }
}
