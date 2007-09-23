begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|CayenneRuntimeException
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
name|ConfigurationException
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
name|access
operator|.
name|DataDomain
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
name|event
operator|.
name|EventManager
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
name|util
operator|.
name|ResourceLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * This class is an entry point to Cayenne. It loads all configuration files and  * instantiates main Cayenne objects. Used as a singleton via the  * {@link #getSharedConfiguration}method.  *<p>  * To use a custom subclass of Configuration, Java applications must call  * {@link #initializeSharedConfiguration}with the subclass as argument. This will create  * and initialize a Configuration singleton instance of the specified class. By default  * {@link DefaultConfiguration}is instantiated.  *</p>  *   * @author Andrus Adamchik  * @author Holger Hoffstaette  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Configuration
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DOMAIN_FILE
init|=
literal|"cayenne.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Class
name|DEFAULT_CONFIGURATION_CLASS
init|=
name|DefaultConfiguration
operator|.
name|class
decl_stmt|;
specifier|protected
specifier|static
name|Configuration
name|sharedConfiguration
decl_stmt|;
comment|/**      * Lookup map that stores DataDomains with names as keys.      */
specifier|protected
name|SortedMap
name|dataDomains
init|=
operator|new
name|TreeMap
argument_list|()
decl_stmt|;
specifier|protected
name|DataSourceFactory
name|overrideFactory
decl_stmt|;
specifier|protected
name|ConfigStatus
name|loadStatus
init|=
operator|new
name|ConfigStatus
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|domainConfigurationName
init|=
name|DEFAULT_DOMAIN_FILE
decl_stmt|;
specifier|protected
name|boolean
name|ignoringLoadFailures
decl_stmt|;
specifier|protected
name|ConfigLoaderDelegate
name|loaderDelegate
decl_stmt|;
specifier|protected
name|ConfigSaverDelegate
name|saverDelegate
decl_stmt|;
specifier|protected
name|ConfigurationShutdownHook
name|configurationShutdownHook
init|=
operator|new
name|ConfigurationShutdownHook
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|dataViewLocations
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|projectVersion
decl_stmt|;
comment|/**      * @since 1.2      */
specifier|protected
name|EventManager
name|eventManager
decl_stmt|;
comment|/**      * Use this method as an entry point to all Cayenne access objects.      *<p>      * Note that if you want to provide a custom Configuration, make sure you call one of      * the {@link #initializeSharedConfiguration}methods before your application code has      * a chance to call this method.      */
specifier|public
specifier|synchronized
specifier|static
name|Configuration
name|getSharedConfiguration
parameter_list|()
block|{
if|if
condition|(
name|Configuration
operator|.
name|sharedConfiguration
operator|==
literal|null
condition|)
block|{
name|Configuration
operator|.
name|initializeSharedConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|Configuration
operator|.
name|sharedConfiguration
return|;
block|}
comment|/**      * Returns EventManager used by this configuration.      *       * @since 1.2      */
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|eventManager
return|;
block|}
comment|/**      * Sets EventManager used by this configuration.      *       * @since 1.2      */
specifier|public
name|void
name|setEventManager
parameter_list|(
name|EventManager
name|eventManager
parameter_list|)
block|{
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
block|}
comment|/**      * Creates and initializes shared Configuration object. By default      * {@link DefaultConfiguration}will be instantiated and assigned to a singleton      * instance of Configuration.      */
specifier|public
specifier|static
name|void
name|initializeSharedConfiguration
parameter_list|()
block|{
name|Configuration
operator|.
name|initializeSharedConfiguration
argument_list|(
name|DEFAULT_CONFIGURATION_CLASS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and initializes a shared Configuration object of a custom Configuration      * subclass.      */
specifier|public
specifier|static
name|void
name|initializeSharedConfiguration
parameter_list|(
name|Class
name|configurationClass
parameter_list|)
block|{
name|Configuration
name|conf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|conf
operator|=
operator|(
name|Configuration
operator|)
name|configurationClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error creating shared Configuration: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error creating shared Configuration."
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|Configuration
operator|.
name|initializeSharedConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the shared Configuration object to a new Configuration object. First calls      * {@link #canInitialize}and - if permitted -{@link #initialize}followed by      * {@link #didInitialize}.      */
specifier|public
specifier|static
name|void
name|initializeSharedConfiguration
parameter_list|(
name|Configuration
name|conf
parameter_list|)
block|{
comment|// check to see whether we can proceed
if|if
condition|(
operator|!
name|conf
operator|.
name|canInitialize
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Configuration of class "
operator|+
name|conf
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" refused to be initialized."
argument_list|)
throw|;
block|}
try|try
block|{
comment|// initialize configuration
name|conf
operator|.
name|initialize
argument_list|()
expr_stmt|;
comment|// call post-initialization hook
name|conf
operator|.
name|didInitialize
argument_list|()
expr_stmt|;
comment|// set the initialized Configuration only after success
name|Configuration
operator|.
name|sharedConfiguration
operator|=
name|conf
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error during Configuration initialization. "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Default constructor for new Configuration instances. Simply calls      * {@link Configuration#Configuration(String)}.      *       * @see Configuration#Configuration(String)      */
specifier|protected
name|Configuration
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default constructor for new Configuration instances using the given resource name      * as the main domain file.      */
specifier|protected
name|Configuration
parameter_list|(
name|String
name|domainConfigurationName
parameter_list|)
block|{
comment|// set domain configuration name
name|this
operator|.
name|setDomainConfigurationName
argument_list|(
name|domainConfigurationName
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
operator|new
name|EventManager
argument_list|()
expr_stmt|;
block|}
comment|/**      * Indicates whether {@link #initialize}can be called. Returning<code>false</code>      * allows new instances to delay or refuse the initialization process.      */
specifier|public
specifier|abstract
name|boolean
name|canInitialize
parameter_list|()
function_decl|;
comment|/**      * Initializes the new instance.      *       * @throws Exception      */
specifier|public
specifier|abstract
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Called after successful completion of {@link #initialize}.      */
specifier|public
specifier|abstract
name|void
name|didInitialize
parameter_list|()
function_decl|;
comment|/**      * Returns the resource locator used for finding and loading resources.      */
specifier|protected
specifier|abstract
name|ResourceLocator
name|getResourceLocator
parameter_list|()
function_decl|;
comment|/**      * Returns a DataDomain as a stream or<code>null</code> if it cannot be found.      */
comment|// TODO: this method is only used in sublcass (DefaultConfiguration),
comment|// should we remove it from here?
specifier|protected
specifier|abstract
name|InputStream
name|getDomainConfiguration
parameter_list|()
function_decl|;
comment|/**      * Returns a DataMap with the given name or<code>null</code> if it cannot be found.      */
specifier|protected
specifier|abstract
name|InputStream
name|getMapConfiguration
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * See 'https://svn.apache.org/repos/asf/cayenne/dataviews/trunk' for DataViews code,      * which is not a part of Cayenne since 3.0.      */
specifier|protected
specifier|abstract
name|InputStream
name|getViewConfiguration
parameter_list|(
name|String
name|location
parameter_list|)
function_decl|;
comment|/**      * Returns the name of the main domain configuration resource. Defaults to      * {@link Configuration#DEFAULT_DOMAIN_FILE}.      */
specifier|public
name|String
name|getDomainConfigurationName
parameter_list|()
block|{
return|return
name|this
operator|.
name|domainConfigurationName
return|;
block|}
comment|/**      * Sets the name of the main domain configuration resource.      *       * @param domainConfigurationName the name of the resource that contains this      *            Configuration's domain(s).      */
specifier|protected
name|void
name|setDomainConfigurationName
parameter_list|(
name|String
name|domainConfigurationName
parameter_list|)
block|{
name|this
operator|.
name|domainConfigurationName
operator|=
name|domainConfigurationName
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|String
name|getProjectVersion
parameter_list|()
block|{
return|return
name|projectVersion
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setProjectVersion
parameter_list|(
name|String
name|projectVersion
parameter_list|)
block|{
name|this
operator|.
name|projectVersion
operator|=
name|projectVersion
expr_stmt|;
block|}
comment|/**      * Returns an internal property for the DataSource factory that will override any      * settings configured in XML. Subclasses may override this method to provide a      * special factory for DataSource creation that will take precedence over any      * factories configured in a cayenne project.      */
specifier|public
name|DataSourceFactory
name|getDataSourceFactory
parameter_list|()
block|{
return|return
name|this
operator|.
name|overrideFactory
return|;
block|}
specifier|public
name|void
name|setDataSourceFactory
parameter_list|(
name|DataSourceFactory
name|overrideFactory
parameter_list|)
block|{
name|this
operator|.
name|overrideFactory
operator|=
name|overrideFactory
expr_stmt|;
block|}
comment|/**      * Adds new DataDomain to the list of registered domains. Injects EventManager used by      * this configuration into the domain.      */
specifier|public
name|void
name|addDomain
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
if|if
condition|(
name|domain
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Attempt to add DataDomain with no name."
argument_list|)
throw|;
block|}
name|Object
name|old
init|=
name|dataDomains
operator|.
name|put
argument_list|(
name|domain
operator|.
name|getName
argument_list|()
argument_list|,
name|domain
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
name|old
operator|!=
name|domain
condition|)
block|{
name|dataDomains
operator|.
name|put
argument_list|(
name|domain
operator|.
name|getName
argument_list|()
argument_list|,
name|old
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to overwrite domain with name "
operator|+
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// inject EventManager
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|domain
operator|.
name|setEventManager
argument_list|(
name|getEventManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|logObj
operator|.
name|debug
argument_list|(
literal|"added domain: "
operator|+
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns registered domain matching<code>name</code> or<code>null</code> if no      * such domain is found.      */
specifier|public
name|DataDomain
name|getDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|DataDomain
operator|)
name|dataDomains
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns default domain of this configuration. If no domains are configured,      *<code>null</code> is returned. If more than one domain exists in this      * configuration, a CayenneRuntimeException is thrown, indicating that the domain name      * must be explicitly specified. In such cases {@link #getDomain(String name)}must be      * used instead.      */
specifier|public
name|DataDomain
name|getDomain
parameter_list|()
block|{
name|int
name|size
init|=
name|dataDomains
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
return|return
operator|(
name|DataDomain
operator|)
name|dataDomains
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"More than one domain is configured; use 'getDomain(String name)' instead."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Unregisters DataDomain matching<code>name<code> from      * this Configuration object. Note that any domain database      * connections remain open, and it is a responsibility of a      * caller to clean it up.      */
specifier|public
name|void
name|removeDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|dataDomains
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|domain
operator|.
name|setEventManager
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns an unmodifiable collection of registered DataDomains sorted by domain name.      */
specifier|public
name|Collection
name|getDomains
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|dataDomains
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns whether to ignore any failures during map loading or not.      *       * @return boolean      */
specifier|public
name|boolean
name|isIgnoringLoadFailures
parameter_list|()
block|{
return|return
name|this
operator|.
name|ignoringLoadFailures
return|;
block|}
comment|/**      * Sets whether to ignore any failures during map loading or not.      *       * @param ignoringLoadFailures<code>true</code> or<code>false</code>      */
specifier|protected
name|void
name|setIgnoringLoadFailures
parameter_list|(
name|boolean
name|ignoringLoadFailures
parameter_list|)
block|{
name|this
operator|.
name|ignoringLoadFailures
operator|=
name|ignoringLoadFailures
expr_stmt|;
block|}
comment|/**      * Returns the load status.      *       * @return ConfigStatus      */
specifier|public
name|ConfigStatus
name|getLoadStatus
parameter_list|()
block|{
return|return
name|this
operator|.
name|loadStatus
return|;
block|}
comment|/**      * Sets the load status.      */
specifier|protected
name|void
name|setLoadStatus
parameter_list|(
name|ConfigStatus
name|status
parameter_list|)
block|{
name|this
operator|.
name|loadStatus
operator|=
name|status
expr_stmt|;
block|}
comment|/**      * Returns a delegate used for controlling the loading of configuration elements.      */
specifier|public
name|ConfigLoaderDelegate
name|getLoaderDelegate
parameter_list|()
block|{
return|return
name|loaderDelegate
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setLoaderDelegate
parameter_list|(
name|ConfigLoaderDelegate
name|loaderDelegate
parameter_list|)
block|{
name|this
operator|.
name|loaderDelegate
operator|=
name|loaderDelegate
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|ConfigSaverDelegate
name|getSaverDelegate
parameter_list|()
block|{
return|return
name|saverDelegate
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setSaverDelegate
parameter_list|(
name|ConfigSaverDelegate
name|saverDelegate
parameter_list|)
block|{
name|this
operator|.
name|saverDelegate
operator|=
name|saverDelegate
expr_stmt|;
block|}
comment|/**      * Initializes configuration with the location of data views.      *       * @since 1.1      * @param dataViewLocations Map of DataView locations.      */
specifier|public
name|void
name|setDataViewLocations
parameter_list|(
name|Map
name|dataViewLocations
parameter_list|)
block|{
if|if
condition|(
name|dataViewLocations
operator|==
literal|null
condition|)
name|this
operator|.
name|dataViewLocations
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
else|else
name|this
operator|.
name|dataViewLocations
operator|=
name|dataViewLocations
expr_stmt|;
block|}
comment|/**      * See 'https://svn.apache.org/repos/asf/cayenne/dataviews/trunk' for DataViews code,      * which is not a part of Cayenne since 3.0.      *       * @since 1.1      */
specifier|public
name|Map
name|getDataViewLocations
parameter_list|()
block|{
return|return
name|dataViewLocations
return|;
block|}
comment|/**      * Shutdowns all owned domains. Invokes DataDomain.shutdown().      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|Collection
name|domains
init|=
name|getDomains
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|domains
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|domain
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|eventManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|ConfigurationShutdownHook
extends|extends
name|Thread
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|installConfigurationShutdownHook
parameter_list|()
block|{
name|uninstallConfigurationShutdownHook
argument_list|()
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|configurationShutdownHook
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|uninstallConfigurationShutdownHook
parameter_list|()
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|removeShutdownHook
argument_list|(
name|configurationShutdownHook
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

