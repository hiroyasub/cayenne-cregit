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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|EntityManagerFactory
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
name|PersistenceProvider
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
name|access
operator|.
name|DataNode
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
name|DbGenerator
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
name|conf
operator|.
name|Configuration
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
name|conf
operator|.
name|ConnectionProperties
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
name|dba
operator|.
name|AutoAdapter
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
name|dba
operator|.
name|DbAdapter
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
name|enhancer
operator|.
name|Enhancer
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
name|instrument
operator|.
name|InstrumentUtil
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
name|bridge
operator|.
name|DataMapConverter
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
name|DefaultDataSourceFactory
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
name|EntityMapLoader
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
name|EntityMapLoaderContext
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
name|UnitLoader
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
name|enhancer
operator|.
name|JpaEnhancerVisitorFactory
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
name|instrument
operator|.
name|InstrumentingUnitFactory
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
name|instrument
operator|.
name|UnitClassTransformer
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
name|map
operator|.
name|JpaClassDescriptor
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
name|reflect
operator|.
name|JpaClassDescriptorFactory
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|DbEntity
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
name|reflect
operator|.
name|ClassDescriptorMap
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
name|cayenne
operator|.
name|validation
operator|.
name|SimpleValidationFailure
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
name|validation
operator|.
name|ValidationResult
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
comment|/**  * A PersistenceProvider implementation based on Cayenne stack. Wraps a Cayenne  * Configuration instance.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|Provider
implements|implements
name|PersistenceProvider
block|{
comment|// spec-defined properties per ch. 7.1.3.1.
specifier|public
specifier|static
specifier|final
name|String
name|PROVIDER_PROPERTY
init|=
literal|"javax.persistence.provider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSACTION_TYPE_PROPERTY
init|=
literal|"javax.persistence.transactionType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JTA_DATA_SOURCE_PROPERTY
init|=
literal|"javax.persistence.jtaDataSource"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_JTA_DATA_SOURCE_PROPERTY
init|=
literal|"javax.persistence.nonJtaDataSource"
decl_stmt|;
comment|// provider-specific properties. Must use provider namespace per ch. 7.1.3.1.
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_SCHEMA_PROPERTY
init|=
literal|"org.apache.cayenne.schema.create"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_FACTORY_PROPERTY
init|=
literal|"org.apache.cayenne.jpa.jpaDataSourceFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNIT_FACTORY_PROPERTY
init|=
literal|"org.apache.cayenne.jpa.jpaUnitFactory"
decl_stmt|;
comment|// ... DataSource
specifier|public
specifier|static
specifier|final
name|String
name|ADAPTER_PROPERTY
init|=
literal|"org.apache.cayenne."
operator|+
name|ConnectionProperties
operator|.
name|ADAPTER_KEY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_DRIVER_PROPERTY
init|=
literal|"org.apache.cayenne.datasource."
operator|+
name|ConnectionProperties
operator|.
name|DRIVER_KEY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_URL_PROPERTY
init|=
literal|"org.apache.cayenne.datasource."
operator|+
name|ConnectionProperties
operator|.
name|URL_KEY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_USER_NAME_PROPERTY
init|=
literal|"org.apache.cayenne.datasource."
operator|+
name|ConnectionProperties
operator|.
name|USER_NAME_KEY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_PASSWORD_PROPERTY
init|=
literal|"org.apache.cayenne.datasource."
operator|+
name|ConnectionProperties
operator|.
name|PASSWORD_KEY
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_MIN_CONNECTIONS_PROPERTY
init|=
literal|"org.apache.cayenne.datasource.jdbc.minConnections"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_SOURCE_MAX_CONNECTIONS_PROPERTY
init|=
literal|"org.apache.cayenne.datasource.jdbc.maxConnections"
decl_stmt|;
specifier|static
specifier|final
name|String
name|INSTRUMENTING_FACTORY_CLASS
init|=
name|InstrumentingUnitFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|protected
name|boolean
name|validateDescriptors
decl_stmt|;
specifier|protected
name|UnitLoader
name|unitLoader
decl_stmt|;
specifier|protected
name|Properties
name|defaultProperties
decl_stmt|;
specifier|protected
name|Configuration
name|configuration
decl_stmt|;
specifier|protected
name|Log
name|logger
decl_stmt|;
comment|/**      * Creates a new PersistenceProvider with properties configured to run in a standalone      * mode with Cayenne stack.      */
specifier|public
name|Provider
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Provider
parameter_list|(
name|boolean
name|validateDescriptors
parameter_list|)
block|{
name|this
operator|.
name|validateDescriptors
operator|=
name|validateDescriptors
expr_stmt|;
name|this
operator|.
name|defaultProperties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|configureEnvironmentProperties
argument_list|()
expr_stmt|;
name|configureDefaultProperties
argument_list|()
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|LazyConfiguration
argument_list|()
expr_stmt|;
comment|// set a singleton that may be used by Cayenne
name|Configuration
operator|.
name|initializeSharedConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Loads default properties from the Java environment.      */
specifier|protected
name|void
name|configureEnvironmentProperties
parameter_list|()
block|{
name|String
name|dsFactory
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|DATA_SOURCE_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|dsFactory
operator|!=
literal|null
condition|)
block|{
name|defaultProperties
operator|.
name|put
argument_list|(
name|DATA_SOURCE_FACTORY_PROPERTY
argument_list|,
name|dsFactory
argument_list|)
expr_stmt|;
block|}
name|String
name|transactionType
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|TRANSACTION_TYPE_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|transactionType
operator|!=
literal|null
condition|)
block|{
name|defaultProperties
operator|.
name|put
argument_list|(
name|TRANSACTION_TYPE_PROPERTY
argument_list|,
name|transactionType
argument_list|)
expr_stmt|;
block|}
name|String
name|unitFactory
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|UNIT_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|unitFactory
operator|==
literal|null
operator|&&
name|InstrumentUtil
operator|.
name|isAgentLoaded
argument_list|()
condition|)
block|{
name|unitFactory
operator|=
name|INSTRUMENTING_FACTORY_CLASS
expr_stmt|;
block|}
if|if
condition|(
name|unitFactory
operator|!=
literal|null
condition|)
block|{
name|defaultProperties
operator|.
name|put
argument_list|(
name|UNIT_FACTORY_PROPERTY
argument_list|,
name|unitFactory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|configureDefaultProperties
parameter_list|()
block|{
if|if
condition|(
operator|!
name|defaultProperties
operator|.
name|containsKey
argument_list|(
name|DATA_SOURCE_FACTORY_PROPERTY
argument_list|)
condition|)
block|{
name|defaultProperties
operator|.
name|put
argument_list|(
name|DATA_SOURCE_FACTORY_PROPERTY
argument_list|,
name|DefaultDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|defaultProperties
operator|.
name|containsKey
argument_list|(
name|TRANSACTION_TYPE_PROPERTY
argument_list|)
condition|)
block|{
name|defaultProperties
operator|.
name|put
argument_list|(
name|TRANSACTION_TYPE_PROPERTY
argument_list|,
name|PersistenceUnitTransactionType
operator|.
name|RESOURCE_LOCAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called by Persistence class when an EntityManagerFactory is to be created. Creates      * a {@link JpaUnit} and calls      * {@link #createContainerEntityManagerFactory(PersistenceUnitInfo, Map)}.      */
specifier|public
name|EntityManagerFactory
name|createEntityManagerFactory
parameter_list|(
name|String
name|emName
parameter_list|,
name|Map
name|map
parameter_list|)
block|{
name|JpaUnit
name|ui
init|=
name|loadUnit
argument_list|(
name|emName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ui
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// override properties
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|ui
operator|.
name|addProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|// set default properties if they are not set explicitly
name|Properties
name|properties
init|=
name|ui
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|property
range|:
name|defaultProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|property
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|property
operator|.
name|getKey
argument_list|()
argument_list|,
name|property
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check if we are allowed to handle this unit (JPA Spec, 7.2)
name|String
name|provider
init|=
name|ui
operator|.
name|getPersistenceProviderClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
operator|&&
operator|!
name|provider
operator|.
name|equals
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// do not pass properties further down, they are already acounted for in the
comment|// PersistenceUnitInfo.
return|return
name|createContainerEntityManagerFactory
argument_list|(
name|ui
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Called by the container when an EntityManagerFactory is to be created. Returns a      * {@link EntityManagerFactory} which is a DataDomain wrapper. Note that Cayenne      * provider will ignore all but 'javax.persistence.transactionType' property in the      * passed property map.      */
specifier|public
specifier|synchronized
name|EntityManagerFactory
name|createContainerEntityManagerFactory
parameter_list|(
name|PersistenceUnitInfo
name|unit
parameter_list|,
name|Map
name|map
parameter_list|)
block|{
name|String
name|name
init|=
name|unit
operator|.
name|getPersistenceUnitName
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|configuration
operator|.
name|getDomain
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 2/3/2007 - considering property overrides, it may be a bad idea
comment|// to cache domains. Essentially we are caching a PersistenceUnitInfo with a given
comment|// name, without a possibility to refresh it. But maybe this is ok...?
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
name|long
name|t0
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|isJTA
init|=
name|isJta
argument_list|(
name|unit
argument_list|,
name|map
argument_list|)
decl_stmt|;
comment|// configure Cayenne domain
name|domain
operator|=
operator|new
name|DataDomain
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|ClassDescriptorMap
name|descriptors
init|=
name|domain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
decl_stmt|;
name|descriptors
operator|.
name|addFactory
argument_list|(
operator|new
name|JpaClassDescriptorFactory
argument_list|(
name|descriptors
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|EntityMapLoader
name|loader
init|=
operator|new
name|EntityMapLoader
argument_list|(
name|unit
argument_list|)
decl_stmt|;
comment|// we must set enhancer in this exact place, between JPA and Cayenne mapping
comment|// loading. By now all the JpaEntities are loaded (using separate unit class
comment|// loader) and Cayenne mapping will be using the App ClassLoader.
name|Map
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
name|managedClasses
init|=
name|loader
operator|.
name|getEntityMap
argument_list|()
operator|.
name|getMangedClasses
argument_list|()
decl_stmt|;
name|unit
operator|.
name|addTransformer
argument_list|(
operator|new
name|UnitClassTransformer
argument_list|(
name|managedClasses
argument_list|,
operator|new
name|Enhancer
argument_list|(
operator|new
name|JpaEnhancerVisitorFactory
argument_list|(
name|managedClasses
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|DataMapConverter
name|converter
init|=
operator|new
name|DataMapConverter
argument_list|()
decl_stmt|;
name|DataMap
name|cayenneMap
init|=
name|converter
operator|.
name|toDataMap
argument_list|(
name|name
argument_list|,
name|loader
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 2/3/2007 - clarify this logic.... JTA EM may not always mean
comment|// JTA DS?
name|DataSource
name|dataSource
init|=
name|isJTA
condition|?
name|unit
operator|.
name|getJtaDataSource
argument_list|()
else|:
name|unit
operator|.
name|getNonJtaDataSource
argument_list|()
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|createCustomAdapter
argument_list|(
name|loader
operator|.
name|getContext
argument_list|()
argument_list|,
name|unit
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
name|adapter
operator|=
operator|new
name|AutoAdapter
argument_list|(
operator|new
name|NodeDataSource
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
name|cayenneMap
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setUsingExternalTransactions
argument_list|(
name|isJTA
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|unit
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
name|CREATE_SCHEMA_PROPERTY
argument_list|)
argument_list|)
condition|)
block|{
name|loadSchema
argument_list|(
name|dataSource
argument_list|,
name|adapter
argument_list|,
name|cayenneMap
argument_list|)
expr_stmt|;
block|}
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// report conflicts...
name|ValidationResult
name|conflicts
init|=
name|loader
operator|.
name|getContext
argument_list|()
operator|.
name|getConflicts
argument_list|()
decl_stmt|;
if|if
condition|(
name|conflicts
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
for|for
control|(
name|Object
name|failure
range|:
name|conflicts
operator|.
name|getFailures
argument_list|()
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"*** mapping conflict: "
operator|+
name|failure
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"loaded persistence unit '"
operator|+
name|name
operator|+
literal|"' in "
operator|+
operator|(
name|t1
operator|-
name|t0
operator|)
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// see TODO above - JTA vs RESOURCE_LOCAL is cached per domain... maybe need to
comment|// change that
return|return
name|domain
operator|.
name|isUsingExternalTransactions
argument_list|()
condition|?
operator|new
name|JtaEntityManagerFactory
argument_list|(
name|this
argument_list|,
name|domain
argument_list|,
name|unit
argument_list|)
else|:
operator|new
name|ResourceLocalEntityManagerFactory
argument_list|(
name|this
argument_list|,
name|domain
argument_list|,
name|unit
argument_list|)
return|;
block|}
comment|/**      * Returns whether provided configuration specifies a JTA or RESOURCE_LOCAL      * EntityManager.      */
specifier|private
name|boolean
name|isJta
parameter_list|(
name|PersistenceUnitInfo
name|unit
parameter_list|,
name|Map
name|overrides
parameter_list|)
block|{
name|PersistenceUnitTransactionType
name|txType
decl_stmt|;
name|String
name|txTypeOverride
init|=
operator|(
name|overrides
operator|!=
literal|null
operator|)
condition|?
operator|(
name|String
operator|)
name|overrides
operator|.
name|get
argument_list|(
name|TRANSACTION_TYPE_PROPERTY
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|txTypeOverride
operator|!=
literal|null
condition|)
block|{
name|txType
operator|=
name|PersistenceUnitTransactionType
operator|.
name|valueOf
argument_list|(
name|txTypeOverride
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|txType
operator|=
name|unit
operator|.
name|getTransactionType
argument_list|()
expr_stmt|;
block|}
return|return
name|txType
operator|==
name|PersistenceUnitTransactionType
operator|.
name|JTA
return|;
block|}
comment|/**      * Loads database schema if it doesn't yet exist.      */
specifier|protected
name|void
name|loadSchema
parameter_list|(
name|DataSource
name|dataSource
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|Collection
name|tables
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
decl_stmt|;
if|if
condition|(
name|tables
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// sniff a first table precense
comment|// TODO: andrus 9/1/2006 - should we make this check a part of DbGenerator (and
comment|// query - a part of DbAdapter)?
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|tables
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|Connection
name|c
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|tableName
init|=
name|table
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
comment|// select all tables to avoid case sensitivity issues.
name|ResultSet
name|rs
init|=
name|c
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
name|table
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|table
operator|.
name|getSchema
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|sqlName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tableName
operator|.
name|equals
argument_list|(
name|sqlName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"table "
operator|+
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|" is present; will skip schema generation."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e1
parameter_list|)
block|{
comment|// db exists
name|logger
operator|.
name|debug
argument_list|(
literal|"error generating schema, assuming schema exists."
argument_list|)
expr_stmt|;
return|return;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"table "
operator|+
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|" is absent; will continue with schema generation."
argument_list|)
expr_stmt|;
comment|// run generator
name|DbGenerator
name|generator
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|)
decl_stmt|;
try|try
block|{
name|generator
operator|.
name|runGenerator
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
block|}
block|}
specifier|protected
name|DbAdapter
name|createCustomAdapter
parameter_list|(
name|EntityMapLoaderContext
name|context
parameter_list|,
name|PersistenceUnitInfo
name|info
parameter_list|)
block|{
name|String
name|adapterClass
init|=
name|info
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
name|ADAPTER_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|adapterClass
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|// adapter class is not enhanced, so use a normal class loader
name|Class
name|dbAdapterClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|adapterClass
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
decl_stmt|;
return|return
operator|(
name|DbAdapter
operator|)
name|dbAdapterClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|info
argument_list|,
literal|"Failed to load adapter '"
operator|+
name|adapterClass
operator|+
literal|"', message: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Loads a named JpaUnit using internal UnitLoader.      */
specifier|protected
name|JpaUnit
name|loadUnit
parameter_list|(
name|String
name|emName
parameter_list|)
block|{
comment|// TODO: Andrus, 2/11/2006 - cache loaded units (or factories)...?
return|return
name|getUnitLoader
argument_list|()
operator|.
name|loadUnit
argument_list|(
name|emName
argument_list|)
return|;
block|}
comment|/**      * Returns unit loader, lazily creating it on first invocation.      */
specifier|protected
name|UnitLoader
name|getUnitLoader
parameter_list|()
block|{
if|if
condition|(
name|unitLoader
operator|==
literal|null
condition|)
block|{
name|JpaUnitFactory
name|factory
init|=
literal|null
decl_stmt|;
name|String
name|unitFactoryName
init|=
name|getDefaultProperty
argument_list|(
name|UNIT_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|unitFactoryName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
name|factoryClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|unitFactoryName
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
decl_stmt|;
name|factory
operator|=
operator|(
name|JpaUnitFactory
operator|)
name|factoryClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Error loading unit infor factory '"
operator|+
name|unitFactoryName
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|this
operator|.
name|unitLoader
operator|=
operator|new
name|UnitLoader
argument_list|(
name|factory
argument_list|,
name|validateDescriptors
argument_list|)
expr_stmt|;
block|}
return|return
name|unitLoader
return|;
block|}
comment|// TODO: andrus, 4/29/2006 - this is copied from non-public conf.NodeDataSource. In
comment|// Cayenne> 1.2 make it public.
class|class
name|NodeDataSource
implements|implements
name|DataSource
block|{
name|DataNode
name|node
decl_stmt|;
name|NodeDataSource
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
return|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|SQLException
block|{
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|SQLException
block|{
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
block|}
specifier|protected
name|String
name|getDefaultProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|defaultProperties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
class|class
name|LazyConfiguration
extends|extends
name|Configuration
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|canInitialize
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|didInitialize
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|protected
name|ResourceLocator
name|getResourceLocator
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|InputStream
name|getDomainConfiguration
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|InputStream
name|getMapConfiguration
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

