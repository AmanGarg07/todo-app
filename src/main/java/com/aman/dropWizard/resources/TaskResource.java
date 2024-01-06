package com.aman.dropWizard.resources;

import com.aman.dropWizard.client.User;
import com.aman.dropWizard.core.Task;
import com.aman.dropWizard.core.TaskValidator;
import com.aman.dropWizard.db.TaskDAO;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    private final TaskDAO taskDAO;
    private final TaskValidator taskValidator;
    public TaskResource(TaskDAO taskDAO) {

        this.taskDAO = taskDAO;
        taskValidator = new TaskValidator();

    }

    @GET
    @UnitOfWork
    @Timed
    public Response getAllTasks(@Auth User user) {

        try {

            List<Task> tasks = taskDAO.getAllTasks();
            return Response.ok(tasks).build();

        }catch(NoResultException e) {

            String message = "No tasks available";
            return Response.ok(message).build();

        }catch (Exception e) {

            return Response.serverError().entity(e.getMessage()).build();

        }
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response findId(@PathParam("id") int id){

        try {

            Task task = taskDAO.getById(id);
            return Response.ok(task).build();

        }catch(NoResultException e) {

            String message = "No task available with ID: " + id;
            return Response.ok(message).build();

        }catch (Exception e) {
            // Handle other exceptions or errors if needed
            return Response.serverError().entity(e.getMessage()).build();

        }
    }

    @POST
    @UnitOfWork
    @Timed
    public  Response addTask(Task task){
        try {

            taskValidator.validateTask(task);
            Task addedTask = taskDAO.addTask(task);
            // Return the added task with a 201 Created status
            return Response.status(Response.Status.CREATED).entity(addedTask).build();

        } catch (BadRequestException e) {

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

        }
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response deleteTaskById(@PathParam("id") int id) {

        Task taskToDelete = taskDAO.getById(id);
        try {

            taskDAO.delete(taskToDelete);
            return Response.ok("Task Deleted").build();

        }catch(NoResultException e) {

            String message = "No task available with ID: " + id;
            return Response.ok(message).build();

        }catch (Exception e) {

            // Handle other exceptions or errors if needed
            return Response.serverError().entity(e.getMessage()).build();

        }
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response updateTask(@PathParam("id") int id, Task updatedTask) {


        try {
            Task existingTask = taskDAO.getById(id);

            if(updatedTask.getDescription()!=null && !updatedTask.getDescription().isEmpty()) {
                existingTask.setDescription(updatedTask.getDescription());
            }
            if(updatedTask.getStartDate() != null) {
                existingTask.setStartDate(updatedTask.getStartDate());
            }
            if(updatedTask.getTargetDate() != null) {
                existingTask.setTargetDate(updatedTask.getTargetDate());
            }
            if(updatedTask.getStatus()!= null && !updatedTask.getStatus().isEmpty()) {
                existingTask.setStatus(updatedTask.getStatus());
            }

            taskDAO.update(existingTask); // Call update method in DAO
            return Response.ok(existingTask).build(); // Return updated task in response

        }catch(NoResultException e) {

            String message = "No task available with ID: " + id;
            return Response.ok(message).build();

        }catch (Exception e) {

            // Handle other exceptions or errors if needed
            return Response.serverError().entity(e.getMessage()).build();

        }
    }
}