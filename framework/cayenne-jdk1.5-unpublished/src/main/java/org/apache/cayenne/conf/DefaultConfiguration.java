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
name|cayenne
operator|.
name|util
operator|.
name|Util
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
comment|/**  * Subclass of Configuration that uses the System CLASSPATH to locate resources.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DefaultConfiguration
extends|extends
name|Configuration
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * the default ResourceLocator used for CLASSPATH loading      */
specifier|private
name|ResourceLocator
name|locator
decl_stmt|;
comment|/**      * Default constructor. Simply calls      * {@link DefaultConfiguration#DefaultConfiguration(String)} with      * {@link Configuration#DEFAULT_DOMAIN_FILE} as argument.      *       * @see Configuration#Configuration()      */
specifier|public
name|DefaultConfiguration
parameter_list|()
block|{
name|this
argument_list|(
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor with a named domain configuration resource. Simply calls      * {@link Configuration#Configuration(String)}.      *       * @throws ConfigurationException when<code>domainConfigurationName</code> is      *<code>null</code>.      * @see Configuration#Configuration(String)      */
specifier|public
name|DefaultConfiguration
parameter_list|(
name|String
name|domainConfigurationName
parameter_list|)
block|{
name|super
argument_list|(
name|domainConfigurationName
argument_list|)
expr_stmt|;
if|if
condition|(
name|domainConfigurationName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"cannot use null as domain file name."
argument_list|)
throw|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"using domain file name: "
operator|+
name|domainConfigurationName
argument_list|)
expr_stmt|;
comment|// configure CLASSPATH-only locator
name|ResourceLocator
name|locator
init|=
operator|new
name|ResourceLocator
argument_list|()
decl_stmt|;
name|locator
operator|.
name|setSkipAbsolutePath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipClasspath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipCurrentDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipHomeDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// add the current Configuration subclass' package as additional path.
if|if
condition|(
operator|!
operator|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|DefaultConfiguration
operator|.
name|class
argument_list|)
operator|)
condition|)
block|{
name|locator
operator|.
name|addClassPath
argument_list|(
name|Util
operator|.
name|getPackagePath
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setResourceLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates DefaultConfiguration with specified cayenne project file name and      * ResourceLocator.      *       * @since 1.2      */
specifier|public
name|DefaultConfiguration
parameter_list|(
name|String
name|domainConfigurationName
parameter_list|,
name|ResourceLocator
name|locator
parameter_list|)
block|{
name|super
argument_list|(
name|domainConfigurationName
argument_list|)
expr_stmt|;
name|setResourceLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a custom path for class path lookups. Format should be "my/package/name"      *<i>without</i> leading "/". This allows for easy customization of custom search      * paths after Constructor invocation:      *       *<pre>      * conf = new DefaultConfiguration();      * conf.addClassPath(&quot;my/package/name&quot;);      * Configuration.initializeSharedConfiguration(conf);      *</pre>      */
specifier|public
name|void
name|addClassPath
parameter_list|(
name|String
name|customPath
parameter_list|)
block|{
name|this
operator|.
name|getResourceLocator
argument_list|()
operator|.
name|addClassPath
argument_list|(
name|customPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given String as a custom path for resource lookups. The path can be      * relative or absolute and is<i>not</i> checked for existence. Depending on the      * underlying ResourceLocator configuration this can for instance be a path in the web      * application context or a filesystem path.      *       * @throws IllegalArgumentException if<code>path</code> is<code>null</code>.      * @since 1.2 moved from subclass - FileConfiguration.      */
specifier|public
name|void
name|addResourcePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|getResourceLocator
argument_list|()
operator|.
name|addFilesystemPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default implementation of {@link Configuration#canInitialize}. Creates a      * ResourceLocator suitable for loading from the CLASSPATH, unless it has already been      * set in a subclass. Always returns<code>true</code>.      */
annotation|@
name|Override
specifier|public
name|boolean
name|canInitialize
parameter_list|()
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"canInitialize started."
argument_list|)
expr_stmt|;
comment|// allow to proceed
return|return
literal|true
return|;
block|}
comment|/**      * Initializes all Cayenne resources. Loads all configured domains and their data      * maps, initializes all domain Nodes and their DataSources.      */
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"initialize starting."
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|this
operator|.
name|getDomainConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"] : Domain configuration file \""
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getDomainConfigurationName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\" is not found."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|ConfigLoaderDelegate
name|delegate
init|=
name|this
operator|.
name|getLoaderDelegate
argument_list|()
decl_stmt|;
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
operator|=
operator|new
name|RuntimeLoadDelegate
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|getLoadStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ConfigLoader
name|loader
init|=
operator|new
name|ConfigLoader
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
try|try
block|{
name|loader
operator|.
name|loadDomains
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|setLoadStatus
argument_list|(
name|delegate
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// log successful initialization
name|logger
operator|.
name|debug
argument_list|(
literal|"initialize finished."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default implementation of {@link Configuration#didInitialize}. Currently does      * nothing except logging.      */
annotation|@
name|Override
specifier|public
name|void
name|didInitialize
parameter_list|()
block|{
comment|// empty default implementation
name|logger
operator|.
name|debug
argument_list|(
literal|"didInitialize finished."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the default ResourceLocator configured for CLASSPATH lookups.      */
annotation|@
name|Override
specifier|protected
name|ResourceLocator
name|getResourceLocator
parameter_list|()
block|{
return|return
name|this
operator|.
name|locator
return|;
block|}
comment|/**      * Sets the specified {@link ResourceLocator}. Currently called from      * {@link #initialize}.      */
specifier|protected
name|void
name|setResourceLocator
parameter_list|(
name|ResourceLocator
name|locator
parameter_list|)
block|{
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
block|}
comment|/**      * Returns the domain configuration as a stream or<code>null</code> if it cannot be      * found. Uses the configured {@link ResourceLocator} to find the file.      */
annotation|@
name|Override
specifier|protected
name|InputStream
name|getDomainConfiguration
parameter_list|()
block|{
return|return
name|locator
operator|.
name|findResourceStream
argument_list|(
name|this
operator|.
name|getDomainConfigurationName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the {@link org.apache.cayenne.map.DataMap} configuration from a      * specified location or<code>null</code> if it cannot be found. Uses the      * configured {@link ResourceLocator} to find the file.      */
annotation|@
name|Override
specifier|protected
name|InputStream
name|getMapConfiguration
parameter_list|(
name|String
name|location
parameter_list|)
block|{
return|return
name|locator
operator|.
name|findResourceStream
argument_list|(
name|location
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|InputStream
name|getViewConfiguration
parameter_list|(
name|String
name|location
parameter_list|)
block|{
return|return
name|locator
operator|.
name|findResourceStream
argument_list|(
name|location
argument_list|)
return|;
block|}
comment|/**      * @see Object#toString()      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": classloader="
argument_list|)
operator|.
name|append
argument_list|(
name|locator
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

