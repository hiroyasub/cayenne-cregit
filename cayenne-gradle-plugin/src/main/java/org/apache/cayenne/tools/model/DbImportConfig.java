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
name|tools
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|LinkedList
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbimport
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|util
operator|.
name|ConfigureUtil
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportConfig
extends|extends
name|SchemaContainer
block|{
specifier|private
name|boolean
name|skipRelationshipsLoading
decl_stmt|;
specifier|private
name|boolean
name|skipPrimaryKeyLoading
decl_stmt|;
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      *<p>      * Automatically tagging each DbEntity with the actual DB catalog/schema (default behavior) may sometimes be undesirable.      * If this is the case then setting forceDataMapCatalog to true will set DbEntity catalog to one in the DataMap.      *</p>      *<p>      * Default value is<b>false</b>.      *</p>      */
specifier|private
name|boolean
name|forceDataMapCatalog
decl_stmt|;
comment|/**      *<p>      * Automatically tagging each DbEntity with the actual DB catalog/schema (default behavior) may sometimes be undesirable.      * If this is the case then setting forceDataMapSchema to true will set DbEntity schema to one in the DataMap.      *</p>      *<p>      * Default value is<b>false</b>.      *</p>      */
specifier|private
name|boolean
name|forceDataMapSchema
decl_stmt|;
comment|/**      *<p>      * A comma-separated list of Perl5 patterns that defines which imported tables should have their primary key columns      * mapped as ObjAttributes.      *</p>      *<p><b>"*"</b> would indicate all tables.</p>      */
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
comment|/**      *<p>      * Object layer naming generator implementation.      * Should be fully qualified Java class name implementing "org.apache.cayenne.dbsync.naming.ObjectNameGenerator".      *</p>      *<p>      * The default is "org.apache.cayenne.dbsync.naming.DefaultObjectNameGenerator".      *</p>      */
specifier|private
name|String
name|namingStrategy
init|=
literal|"org.apache.cayenne.dbsync.naming.DefaultObjectNameGenerator"
decl_stmt|;
comment|/**      * A regular expression that should match the part of the table name to strip before generating DB names.      */
specifier|private
name|String
name|stripFromTableNames
init|=
literal|""
decl_stmt|;
comment|/**      *<p>If true, would use primitives instead of numeric and boolean classes.</p>      *<p>Default is<b>"true"</b>, i.e. primitives will be used.</p>      */
specifier|private
name|boolean
name|usePrimitives
init|=
literal|true
decl_stmt|;
comment|/**      * Use old Java 7 date types      */
specifier|private
name|boolean
name|useJava7Types
init|=
literal|false
decl_stmt|;
comment|/**      * Typical types are:<ul>      *<li> "TABLE"      *<li> "VIEW"      *<li> "SYSTEM TABLE"      *<li> "GLOBAL TEMPORARY",      *<li> "LOCAL TEMPORARY"      *<li> "ALIAS"      *<li> "SYNONYM"      *</ul>      */
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|tableTypes
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|SchemaContainer
argument_list|>
name|catalogs
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|catalog
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|catalogs
operator|.
name|add
argument_list|(
operator|new
name|SchemaContainer
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|catalog
parameter_list|(
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|catalogs
operator|.
name|add
argument_list|(
name|ConfigureUtil
operator|.
name|configure
argument_list|(
name|closure
argument_list|,
operator|new
name|SchemaContainer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|catalog
parameter_list|(
name|String
name|name
parameter_list|,
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|catalogs
operator|.
name|add
argument_list|(
name|ConfigureUtil
operator|.
name|configure
argument_list|(
name|closure
argument_list|,
operator|new
name|SchemaContainer
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReverseEngineering
name|toReverseEngineering
parameter_list|()
block|{
specifier|final
name|ReverseEngineering
name|reverseEngineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|reverseEngineering
operator|.
name|setSkipRelationshipsLoading
argument_list|(
name|skipRelationshipsLoading
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setForceDataMapCatalog
argument_list|(
name|forceDataMapCatalog
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setForceDataMapSchema
argument_list|(
name|forceDataMapSchema
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setMeaningfulPkTables
argument_list|(
name|meaningfulPkTables
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setStripFromTableNames
argument_list|(
name|stripFromTableNames
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setUsePrimitives
argument_list|(
name|usePrimitives
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setUseJava7Types
argument_list|(
name|useJava7Types
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setTableTypes
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
for|for
control|(
name|SchemaContainer
name|catalog
range|:
name|catalogs
control|)
block|{
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog
operator|.
name|toCatalog
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FilterContainer
name|schema
range|:
name|schemas
control|)
block|{
name|reverseEngineering
operator|.
name|addSchema
argument_list|(
name|schema
operator|.
name|fillContainer
argument_list|(
operator|new
name|Schema
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|fillContainer
argument_list|(
name|reverseEngineering
argument_list|)
expr_stmt|;
return|return
name|reverseEngineering
return|;
block|}
specifier|public
name|boolean
name|isSkipRelationshipsLoading
parameter_list|()
block|{
return|return
name|skipRelationshipsLoading
return|;
block|}
specifier|public
name|void
name|setSkipRelationshipsLoading
parameter_list|(
name|boolean
name|skipRelationshipsLoading
parameter_list|)
block|{
name|this
operator|.
name|skipRelationshipsLoading
operator|=
name|skipRelationshipsLoading
expr_stmt|;
block|}
specifier|public
name|void
name|skipRelationshipsLoading
parameter_list|(
name|boolean
name|skipRelationshipsLoading
parameter_list|)
block|{
name|setSkipRelationshipsLoading
argument_list|(
name|skipRelationshipsLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSkipPrimaryKeyLoading
parameter_list|()
block|{
return|return
name|skipPrimaryKeyLoading
return|;
block|}
specifier|public
name|void
name|setSkipPrimaryKeyLoading
parameter_list|(
name|boolean
name|skipPrimaryKeyLoading
parameter_list|)
block|{
name|this
operator|.
name|skipPrimaryKeyLoading
operator|=
name|skipPrimaryKeyLoading
expr_stmt|;
block|}
specifier|public
name|void
name|skipPrimaryKeyLoading
parameter_list|(
name|boolean
name|skipPrimaryKeyLoading
parameter_list|)
block|{
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
block|}
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
name|void
name|defaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
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
name|void
name|forceDataMapCatalog
parameter_list|(
name|boolean
name|forceDataMapCatalog
parameter_list|)
block|{
name|setForceDataMapCatalog
argument_list|(
name|forceDataMapCatalog
argument_list|)
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
name|void
name|forceDataMapSchema
parameter_list|(
name|boolean
name|forceDataMapSchema
parameter_list|)
block|{
name|setForceDataMapSchema
argument_list|(
name|forceDataMapSchema
argument_list|)
expr_stmt|;
block|}
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
name|void
name|meaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
name|setMeaningfulPkTables
argument_list|(
name|meaningfulPkTables
argument_list|)
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
specifier|public
name|void
name|namingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
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
name|void
name|stripFromTableNames
parameter_list|(
name|String
name|stripFromTableNames
parameter_list|)
block|{
name|setStripFromTableNames
argument_list|(
name|stripFromTableNames
argument_list|)
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
name|void
name|usePrimitives
parameter_list|(
name|boolean
name|usePrimitives
parameter_list|)
block|{
name|setUsePrimitives
argument_list|(
name|usePrimitives
argument_list|)
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
name|void
name|useJava7Types
parameter_list|(
name|boolean
name|useJava7Types
parameter_list|)
block|{
name|setUseJava7Types
argument_list|(
name|useJava7Types
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getTableTypes
parameter_list|()
block|{
return|return
name|tableTypes
return|;
block|}
specifier|public
name|void
name|setTableTypes
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|tableTypes
parameter_list|)
block|{
name|this
operator|.
name|tableTypes
operator|=
name|tableTypes
expr_stmt|;
block|}
specifier|public
name|void
name|tableType
parameter_list|(
name|String
name|tableType
parameter_list|)
block|{
name|tableTypes
operator|.
name|add
argument_list|(
name|tableType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tableTypes
parameter_list|(
name|String
modifier|...
name|tableTypes
parameter_list|)
block|{
name|this
operator|.
name|tableTypes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|tableTypes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

