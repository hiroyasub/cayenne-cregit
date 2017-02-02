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
name|Serializable
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineering
extends|extends
name|SchemaContainer
implements|implements
name|Serializable
block|{
specifier|private
name|Boolean
name|skipRelationshipsLoading
decl_stmt|;
specifier|private
name|Boolean
name|skipPrimaryKeyLoading
decl_stmt|;
comment|/*      *<p>      * A default package for ObjEntity Java classes.      *</p>      *<p>      * If not specified, and the existing DataMap already has the default package,      * the existing package will be used.      *</p>      */
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
specifier|final
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
specifier|final
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogCollection
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ReverseEngineering
parameter_list|()
block|{
block|}
specifier|public
name|Boolean
name|getSkipRelationshipsLoading
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
name|Boolean
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
name|Boolean
name|getSkipPrimaryKeyLoading
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
name|Boolean
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
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|getCatalogs
parameter_list|()
block|{
return|return
name|catalogCollection
return|;
block|}
specifier|public
name|String
index|[]
name|getTableTypes
parameter_list|()
block|{
return|return
name|tableTypes
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|tableTypes
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/*      * Typical types are "TABLE",      * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",      * "LOCAL TEMPORARY", "ALIAS", "SYNONYM"., etc.      */
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
operator|.
name|addAll
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
block|}
comment|/*      * Typical types are "TABLE",      * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",      * "LOCAL TEMPORARY", "ALIAS", "SYNONYM"., etc.      */
specifier|public
name|void
name|addTableType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|tableTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCatalog
parameter_list|(
name|Catalog
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalogCollection
operator|.
name|add
argument_list|(
name|catalog
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
argument_list|()
decl_stmt|;
name|res
operator|.
name|append
argument_list|(
literal|"ReverseEngineering: "
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isBlank
argument_list|(
name|catalogCollection
argument_list|)
condition|)
block|{
for|for
control|(
name|Catalog
name|catalog
range|:
name|catalogCollection
control|)
block|{
name|catalog
operator|.
name|toString
argument_list|(
name|res
argument_list|,
literal|"  "
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|skipRelationshipsLoading
operator|!=
literal|null
operator|&&
name|skipRelationshipsLoading
condition|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"\n        Skip Relationships Loading"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipPrimaryKeyLoading
operator|!=
literal|null
operator|&&
name|skipPrimaryKeyLoading
condition|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"\n        Skip PrimaryKey Loading"
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|toString
argument_list|(
name|res
argument_list|,
literal|"  "
argument_list|)
operator|.
name|toString
argument_list|()
return|;
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
name|boolean
name|isForceDataMapCatalog
parameter_list|()
block|{
return|return
name|forceDataMapCatalog
return|;
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
name|String
name|getMeaningfulPkTables
parameter_list|()
block|{
return|return
name|meaningfulPkTables
return|;
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
name|String
name|getStripFromTableNames
parameter_list|()
block|{
return|return
name|stripFromTableNames
return|;
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
name|boolean
name|isUseJava7Types
parameter_list|()
block|{
return|return
name|useJava7Types
return|;
block|}
block|}
end_class

end_unit

