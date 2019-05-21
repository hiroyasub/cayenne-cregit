begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|conn
operator|.
name|DataSourceInfo
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
name|dbsync
operator|.
name|filter
operator|.
name|NameFilter
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
name|dbsync
operator|.
name|filter
operator|.
name|NamePatternMatcher
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
name|dbsync
operator|.
name|naming
operator|.
name|DbEntityNameStemmer
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
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|dbsync
operator|.
name|naming
operator|.
name|NoStemStemmer
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
name|dbsync
operator|.
name|naming
operator|.
name|ObjectNameGenerator
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
name|dbsync
operator|.
name|naming
operator|.
name|PatternStemmer
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DbLoaderConfiguration
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DbLoaderDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DefaultDbLoaderDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|DefaultModelMergeDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|LoggingDbLoaderDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
operator|.
name|ModelMergeDelegate
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|FiltersConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportConfiguration
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DATA_MAP_LOCATION_SUFFIX
init|=
literal|".map.xml"
decl_stmt|;
specifier|private
specifier|final
name|DataSourceInfo
name|dataSourceInfo
decl_stmt|;
specifier|private
specifier|final
name|DbLoaderConfiguration
name|dbLoaderConfiguration
decl_stmt|;
specifier|private
name|File
name|targetDataMap
decl_stmt|;
specifier|private
name|String
name|defaultPackage
decl_stmt|;
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
specifier|private
name|String
name|adapter
decl_stmt|;
specifier|private
name|boolean
name|usePrimitives
decl_stmt|;
specifier|private
name|boolean
name|useJava7Types
decl_stmt|;
specifier|private
name|Logger
name|logger
decl_stmt|;
specifier|private
name|String
name|namingStrategy
decl_stmt|;
specifier|private
name|String
name|stripFromTableNames
decl_stmt|;
specifier|private
name|boolean
name|forceDataMapCatalog
decl_stmt|;
specifier|private
name|boolean
name|forceDataMapSchema
decl_stmt|;
specifier|private
name|boolean
name|useDataMapReverseEngineering
decl_stmt|;
specifier|private
name|File
name|cayenneProject
decl_stmt|;
specifier|public
name|DbImportConfiguration
parameter_list|()
block|{
name|this
operator|.
name|dataSourceInfo
operator|=
operator|new
name|DataSourceInfo
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbLoaderConfiguration
operator|=
operator|new
name|DbLoaderConfiguration
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getStripFromTableNames
parameter_list|()
block|{
return|return
name|stripFromTableNames
return|;
block|}
specifier|public
name|void
name|setStripFromTableNames
parameter_list|(
name|String
name|stripFromTableNames
parameter_list|)
block|{
name|this
operator|.
name|stripFromTableNames
operator|=
name|stripFromTableNames
expr_stmt|;
block|}
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/**      * Returns DataMap XML file representing the target of the DB import operation.      */
specifier|public
name|File
name|getTargetDataMap
parameter_list|()
block|{
return|return
name|targetDataMap
return|;
block|}
specifier|public
name|void
name|setTargetDataMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|targetDataMap
operator|=
name|map
expr_stmt|;
block|}
comment|/**      * Returns a default package for ObjEntity Java classes.      */
specifier|public
name|String
name|getDefaultPackage
parameter_list|()
block|{
return|return
name|defaultPackage
return|;
block|}
specifier|public
name|void
name|setDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|this
operator|.
name|defaultPackage
operator|=
name|defaultPackage
expr_stmt|;
block|}
specifier|public
name|String
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
expr_stmt|;
block|}
comment|/**      * Returns the name of a Java class implementing {@link DbAdapter}. This attribute is optional, the default is      * {@link org.apache.cayenne.dba.AutoAdapter}, i.e. Cayenne will try to guess the DB type.      */
specifier|public
name|String
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/**      * Returns a comma-separated list of Perl5 regular expressions that match      * table names for which {@link DbImportAction} should include ObjAttribute      * for PK.      */
specifier|public
name|String
name|getMeaningfulPkTables
parameter_list|()
block|{
return|return
name|meaningfulPkTables
return|;
block|}
specifier|public
name|void
name|setMeaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
name|this
operator|.
name|meaningfulPkTables
operator|=
name|meaningfulPkTables
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsePrimitives
parameter_list|()
block|{
return|return
name|usePrimitives
return|;
block|}
specifier|public
name|void
name|setUsePrimitives
parameter_list|(
name|boolean
name|usePrimitives
parameter_list|)
block|{
name|this
operator|.
name|usePrimitives
operator|=
name|usePrimitives
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseJava7Types
parameter_list|()
block|{
return|return
name|useJava7Types
return|;
block|}
specifier|public
name|void
name|setUseJava7Types
parameter_list|(
name|boolean
name|useJava7Types
parameter_list|)
block|{
name|this
operator|.
name|useJava7Types
operator|=
name|useJava7Types
expr_stmt|;
block|}
specifier|public
name|File
name|getCayenneProject
parameter_list|()
block|{
return|return
name|cayenneProject
return|;
block|}
specifier|public
name|void
name|setCayenneProject
parameter_list|(
name|File
name|cayenneProject
parameter_list|)
block|{
name|this
operator|.
name|cayenneProject
operator|=
name|cayenneProject
expr_stmt|;
block|}
specifier|public
name|NameFilter
name|createMeaningfulPKFilter
parameter_list|()
block|{
if|if
condition|(
name|meaningfulPkTables
operator|==
literal|null
condition|)
block|{
return|return
name|NamePatternMatcher
operator|.
name|EXCLUDE_ALL
return|;
block|}
comment|// TODO: this filter can't handle table names with comma in them
name|String
index|[]
name|patternStrings
init|=
name|meaningfulPkTables
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Pattern
index|[]
name|patterns
init|=
operator|new
name|Pattern
index|[
name|patternStrings
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|patterns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|patterns
index|[
name|i
index|]
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|patternStrings
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|NamePatternMatcher
argument_list|(
name|patterns
argument_list|,
operator|new
name|Pattern
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|public
name|ObjectNameGenerator
name|createNameGenerator
parameter_list|()
block|{
comment|// TODO: not a singleton; called from different places...
comment|// custom name generator
comment|// TODO: support stemmer in non-standard generators...
comment|// TODO: load via DI AdhocObjectFactory
name|String
name|namingStrategy
init|=
name|getNamingStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|namingStrategy
operator|!=
literal|null
operator|&&
operator|!
name|namingStrategy
operator|.
name|equals
argument_list|(
name|DefaultObjectNameGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|generatorClass
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|namingStrategy
argument_list|)
decl_stmt|;
return|return
operator|(
name|ObjectNameGenerator
operator|)
name|generatorClass
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating name generator: "
operator|+
name|namingStrategy
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|createStemmer
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|DbEntityNameStemmer
name|createStemmer
parameter_list|()
block|{
return|return
operator|(
name|stripFromTableNames
operator|==
literal|null
operator|||
name|stripFromTableNames
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|?
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
else|:
operator|new
name|PatternStemmer
argument_list|(
name|stripFromTableNames
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
return|;
block|}
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|jdbcDriver
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setJdbcDriver
argument_list|(
name|jdbcDriver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|dataSourceUrl
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|dataSourceUrl
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataNodeDescriptor
name|createDataNodeDescriptor
parameter_list|()
block|{
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceDescriptor
argument_list|(
name|dataSourceInfo
argument_list|)
expr_stmt|;
return|return
name|nodeDescriptor
return|;
block|}
specifier|public
name|String
name|getDataMapName
parameter_list|()
block|{
name|String
name|name
init|=
name|targetDataMap
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
name|DATA_MAP_LOCATION_SUFFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"DataMap file name must end with '%s': '%s'"
argument_list|,
name|DATA_MAP_LOCATION_SUFFIX
argument_list|,
name|name
argument_list|)
throw|;
block|}
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
name|DATA_MAP_LOCATION_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ModelMergeDelegate
name|createMergeDelegate
parameter_list|()
block|{
return|return
operator|new
name|DefaultModelMergeDelegate
argument_list|()
return|;
block|}
specifier|public
name|DbLoaderDelegate
name|createLoaderDelegate
parameter_list|()
block|{
if|if
condition|(
name|getLogger
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|LoggingDbLoaderDelegate
argument_list|(
name|getLogger
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns configuration that should be used for DB import stage when the schema is loaded from the database.      */
specifier|public
name|DbLoaderConfiguration
name|getDbLoaderConfig
parameter_list|()
block|{
return|return
name|dbLoaderConfiguration
return|;
block|}
specifier|public
name|void
name|setFiltersConfig
parameter_list|(
name|FiltersConfig
name|filtersConfig
parameter_list|)
block|{
name|dbLoaderConfiguration
operator|.
name|setFiltersConfig
argument_list|(
name|filtersConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Importer options:"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|dbLoaderConfiguration
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"    "
argument_list|)
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|res
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|DataSourceInfo
name|getDataSourceInfo
parameter_list|()
block|{
return|return
name|dataSourceInfo
return|;
block|}
specifier|public
name|void
name|setSkipRelationshipsLoading
parameter_list|(
name|Boolean
name|skipRelationshipsLoading
parameter_list|)
block|{
name|this
operator|.
name|dbLoaderConfiguration
operator|.
name|setSkipRelationshipsLoading
argument_list|(
name|skipRelationshipsLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSkipPrimaryKeyLoading
parameter_list|(
name|Boolean
name|skipPrimaryKeyLoading
parameter_list|)
block|{
name|this
operator|.
name|dbLoaderConfiguration
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTableTypes
parameter_list|(
name|String
index|[]
name|tableTypes
parameter_list|)
block|{
name|dbLoaderConfiguration
operator|.
name|setTableTypes
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setForceDataMapCatalog
parameter_list|(
name|boolean
name|forceDataMapCatalog
parameter_list|)
block|{
name|this
operator|.
name|forceDataMapCatalog
operator|=
name|forceDataMapCatalog
expr_stmt|;
block|}
specifier|public
name|boolean
name|isForceDataMapCatalog
parameter_list|()
block|{
return|return
name|forceDataMapCatalog
return|;
block|}
specifier|public
name|void
name|setForceDataMapSchema
parameter_list|(
name|boolean
name|forceDataMapSchema
parameter_list|)
block|{
name|this
operator|.
name|forceDataMapSchema
operator|=
name|forceDataMapSchema
expr_stmt|;
block|}
specifier|public
name|boolean
name|isForceDataMapSchema
parameter_list|()
block|{
return|return
name|forceDataMapSchema
return|;
block|}
specifier|public
name|boolean
name|isUseDataMapReverseEngineering
parameter_list|()
block|{
return|return
name|useDataMapReverseEngineering
return|;
block|}
specifier|public
name|void
name|setUseDataMapReverseEngineering
parameter_list|(
name|boolean
name|useDataMapReverseEngineering
parameter_list|)
block|{
name|this
operator|.
name|useDataMapReverseEngineering
operator|=
name|useDataMapReverseEngineering
expr_stmt|;
block|}
block|}
end_class

end_unit

