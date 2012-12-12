begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|web
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|BaseContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|CayenneRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ObjectContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Module
import|;
end_import

begin_comment
comment|/**  *<p>Stateless implementation of {@link RequestHandler} that creates a new  * {@link ObjectContext} for each request and binds it to the request thread.  *<p>  * This is an alternative to the session-based request handler   * {@link SessionContextRequestHandler} which is the default.  *<p>  * The request handler can be used by injecting it with a custom @{link Module}, like so:  *<pre><code> import org.apache.cayenne.configuration.web.RequestHandler; import org.apache.cayenne.configuration.web.StatelessContextRequestHandler; import org.apache.cayenne.di.Binder; import org.apache.cayenne.di.Module; public class AppModule implements Module {     public void configure(Binder binder) {         binder.bind(RequestHandler.class).to(StatelessContextRequestHandler.class);     } }</code></pre>  *   * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|StatelessContextRequestHandler
implements|implements
name|RequestHandler
block|{
comment|// using injector to lookup services instead of injecting them directly for lazy
comment|// startup and "late binding"
annotation|@
name|Inject
specifier|private
name|Injector
name|injector
decl_stmt|;
specifier|public
name|void
name|requestStart
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|requestEnd
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

