package com.aman.dropWizard;

import com.aman.dropWizard.client.AppAuthorizer;
import com.aman.dropWizard.client.AppBasicAuthenticator;
import com.aman.dropWizard.client.User;
import com.aman.dropWizard.core.Task;
import com.aman.dropWizard.db.TaskDAO;
import com.aman.dropWizard.resources.TaskResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.hibernate.HibernateBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


public class myAppApplication extends Application<myAppConfiguration> {

    public static void main(final String[] args) throws Exception {
        new myAppApplication().run(args);
    }

    @Override
    public String getName() {
        return "myApp";
    }

    @Override
    public void run(final myAppConfiguration configuration,
                    final Environment environment) {

        final FilterRegistration.Dynamic cors = environment.servlets().
                        addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
                                    "GET,PUT,POST,DELETE,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM,
                                            "http://localhost:3000");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                        "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
                                                true, "/*");
        environment.jersey().setUrlPattern("/api/*");

        // Register resources with jersey
        TaskDAO taskDao = new TaskDAO(hibernate.getSessionFactory());
        final TaskResource resource = new TaskResource(taskDao);
        environment.jersey().register(resource);





        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppBasicAuthenticator())
                .setAuthorizer(new AppAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));



    }

    private final HibernateBundle<myAppConfiguration> hibernate = new HibernateBundle<myAppConfiguration>(Task.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(myAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };


    @Override
    public void initialize(final Bootstrap<myAppConfiguration> bootstrap) {

        bootstrap.addBundle(new AssetsBundle("/assets","/", "index.html"));
        bootstrap.addBundle(hibernate);

    }
}
