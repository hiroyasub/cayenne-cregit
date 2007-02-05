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
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|spi
operator|.
name|ClassTransformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|spi
operator|.
name|PersistenceUnitInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|spi
operator|.
name|PersistenceUnitTransactionType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|jpa
operator|.
name|conf
operator|.
name|JpaDataSourceFactory
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
name|jpa
operator|.
name|conf
operator|.
name|JpaUnitFactory
import|;
end_import

begin_comment
comment|/**  * A<code>javax.persistence.spi.PersistenceUnitInfo</code> implementor used by Cayenne  * JPA provider.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaUnit
implements|implements
name|PersistenceUnitInfo
block|{
specifier|protected
name|String
name|persistenceUnitName
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|mappingFileNames
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|URL
argument_list|>
name|jarFileUrls
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|managedClassNames
decl_stmt|;
specifier|protected
name|URL
name|persistenceUnitRootUrl
decl_stmt|;
specifier|protected
name|boolean
name|excludeUnlistedClasses
decl_stmt|;
specifier|protected
name|Properties
name|properties
decl_stmt|;
specifier|protected
name|String
name|description
decl_stmt|;
comment|// properties not exposed directly
specifier|protected
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|public
name|JpaUnit
parameter_list|()
block|{
name|this
operator|.
name|mappingFileNames
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|this
operator|.
name|jarFileUrls
operator|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|this
operator|.
name|managedClassNames
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|setDefaultClassLoader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getPersistenceUnitName
parameter_list|()
block|{
return|return
name|persistenceUnitName
return|;
block|}
specifier|public
name|String
name|getPersistenceProviderClassName
parameter_list|()
block|{
return|return
name|getProperty
argument_list|(
name|Provider
operator|.
name|PROVIDER_PROPERTY
argument_list|)
return|;
block|}
comment|/**      * Adds a {@link ClassTransformer} to the persistence unit. Default implementation      * does nothing, although a provider can defines a {@link JpaUnitFactory} to integrate      * with its own class loading mechanism.      *<h3>JPA Specification, 7.1.4:</h3>      * Add a transformer supplied by the provider that will be called for every new class      * definition or class redefinition that gets loaded by the loader returned by the      * PersistenceInfo.getClassLoader method. The transformer has no effect on the result      * returned by the PersistenceInfo.getTempClassLoader method. Classes are only      * transformed once within the same classloading scope, regardless of how many      * persistence units they may be a part of.      *       * @param transformer A provider-supplied transformer that the Container invokes at      *            class-(re)definition time      */
specifier|public
name|void
name|addTransformer
parameter_list|(
name|ClassTransformer
name|transformer
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|PersistenceUnitTransactionType
name|getTransactionType
parameter_list|()
block|{
name|String
name|type
init|=
name|getProperty
argument_list|(
name|Provider
operator|.
name|TRANSACTION_TYPE_PROPERTY
argument_list|)
decl_stmt|;
comment|// default JTA type is somewhat arbitrary as application-managed EntityManagers
comment|// will use resource-local, while container-managed will use JTA. Normally whoever
comment|// created this unit will set the right value.
return|return
name|type
operator|!=
literal|null
condition|?
name|PersistenceUnitTransactionType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
else|:
name|PersistenceUnitTransactionType
operator|.
name|JTA
return|;
block|}
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
name|JpaDataSourceFactory
name|getJpaDataSourceFactory
parameter_list|()
block|{
name|String
name|factory
init|=
name|getProperty
argument_list|(
name|Provider
operator|.
name|DATA_SOURCE_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"No value for '"
operator|+
name|Provider
operator|.
name|DATA_SOURCE_FACTORY_PROPERTY
operator|+
literal|"' property - can't build DataSource factory."
argument_list|)
throw|;
block|}
try|try
block|{
comment|// use app class loader - this is not the class to enhance...
return|return
operator|(
name|JpaDataSourceFactory
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|factory
argument_list|,
literal|true
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Error instantiating a JPADataSourceFactory: "
operator|+
name|factory
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
specifier|public
name|DataSource
name|getJtaDataSource
parameter_list|()
block|{
name|String
name|name
init|=
name|getProperty
argument_list|(
name|Provider
operator|.
name|JTA_DATA_SOURCE_PROPERTY
argument_list|)
decl_stmt|;
return|return
name|getJpaDataSourceFactory
argument_list|()
operator|.
name|getJtaDataSource
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|public
name|DataSource
name|getNonJtaDataSource
parameter_list|()
block|{
name|String
name|name
init|=
name|getProperty
argument_list|(
name|Provider
operator|.
name|NON_JTA_DATA_SOURCE_PROPERTY
argument_list|)
decl_stmt|;
return|return
name|getJpaDataSourceFactory
argument_list|()
operator|.
name|getNonJtaDataSource
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMappingFileNames
parameter_list|()
block|{
return|return
name|mappingFileNames
return|;
block|}
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|getJarFileUrls
parameter_list|()
block|{
return|return
name|jarFileUrls
return|;
block|}
specifier|public
name|URL
name|getPersistenceUnitRootUrl
parameter_list|()
block|{
return|return
name|persistenceUnitRootUrl
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedClassNames
parameter_list|()
block|{
return|return
name|managedClassNames
return|;
block|}
comment|/**      * Returns whether classes not listed in the persistence.xml descriptor file should be      * excluded from persistence unit. Should be ignored in J2SE environment.      */
specifier|public
name|boolean
name|excludeUnlistedClasses
parameter_list|()
block|{
return|return
name|excludeUnlistedClasses
return|;
block|}
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|classLoader
return|;
block|}
comment|/**      * Creates and returns a child of the main unit ClassLoader.      */
specifier|public
name|ClassLoader
name|getNewTempClassLoader
parameter_list|()
block|{
return|return
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[
literal|0
index|]
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
specifier|public
name|void
name|setExcludeUnlistedClasses
parameter_list|(
name|boolean
name|excludeUnlistedClasses
parameter_list|)
block|{
name|this
operator|.
name|excludeUnlistedClasses
operator|=
name|excludeUnlistedClasses
expr_stmt|;
block|}
specifier|public
name|void
name|addJarFileUrl
parameter_list|(
name|String
name|jarName
parameter_list|)
block|{
comment|// resolve URLs relative to the unit root
if|if
condition|(
name|persistenceUnitRootUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Persistence Unit Root URL is not set"
argument_list|)
throw|;
block|}
try|try
block|{
name|this
operator|.
name|jarFileUrls
operator|.
name|add
argument_list|(
operator|new
name|URL
argument_list|(
name|persistenceUnitRootUrl
argument_list|,
name|jarName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid Jar file name:"
operator|+
name|jarName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setPersistenceUnitName
parameter_list|(
name|String
name|persistenceUnitName
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnitName
operator|=
name|persistenceUnitName
expr_stmt|;
block|}
comment|/**      * Sets new "main" ClassLoader of this unit.      */
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|setDefaultClassLoader
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setDefaultClassLoader
parameter_list|()
block|{
name|this
operator|.
name|classLoader
operator|=
operator|new
name|JpaUnitClassLoader
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addManagedClassName
parameter_list|(
name|String
name|managedClassName
parameter_list|)
block|{
name|this
operator|.
name|managedClassNames
operator|.
name|add
argument_list|(
name|managedClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addMappingFileName
parameter_list|(
name|String
name|mappingFileName
parameter_list|)
block|{
name|this
operator|.
name|mappingFileNames
operator|.
name|add
argument_list|(
name|mappingFileName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPersistenceUnitRootUrl
parameter_list|(
name|URL
name|persistenceUnitRootUrl
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnitRootUrl
operator|=
name|persistenceUnitRootUrl
expr_stmt|;
block|}
specifier|public
name|void
name|addProperties
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
block|}
end_class

end_unit

