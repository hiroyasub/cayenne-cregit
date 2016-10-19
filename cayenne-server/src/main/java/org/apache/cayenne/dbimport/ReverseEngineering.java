begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|*
import|;
end_import

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
comment|/**  * @since 4.0.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"reverseEngineering"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
class|class
name|ReverseEngineering
extends|extends
name|FilterContainer
implements|implements
name|Serializable
block|{
annotation|@
name|XmlTransient
specifier|protected
name|Resource
name|configurationSource
decl_stmt|;
annotation|@
name|XmlTransient
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Boolean
name|skipRelationshipsLoading
decl_stmt|;
specifier|private
name|Boolean
name|skipPrimaryKeyLoading
decl_stmt|;
comment|/*      * Typical types are "TABLE",      * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",      * "LOCAL TEMPORARY", "ALIAS", "SYNONYM"., etc.      */
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"tableType"
argument_list|)
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|tableTypes
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"catalog"
argument_list|)
specifier|private
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
init|=
operator|new
name|LinkedList
argument_list|<
name|Catalog
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"schema"
argument_list|)
specifier|private
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
operator|new
name|LinkedList
argument_list|<
name|Schema
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ReverseEngineering
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
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
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
name|catalogs
return|;
block|}
specifier|public
name|void
name|setCatalogs
parameter_list|(
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
parameter_list|)
block|{
name|this
operator|.
name|catalogs
operator|=
name|catalogs
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|Schema
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|=
name|schemas
expr_stmt|;
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
operator|=
name|tableTypes
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
name|addSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|.
name|add
argument_list|(
name|schema
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
name|catalogs
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
name|catalogs
argument_list|)
condition|)
block|{
for|for
control|(
name|Catalog
name|catalog
range|:
name|catalogs
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
operator|!
name|isBlank
argument_list|(
name|schemas
argument_list|)
condition|)
block|{
for|for
control|(
name|Schema
name|schema
range|:
name|schemas
control|)
block|{
name|schema
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
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"        Skip Relationships Loading"
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
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"        Skip PrimaryKey Loading"
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
comment|/**      * @since 4.0      */
specifier|public
name|Resource
name|getConfigurationSource
parameter_list|()
block|{
return|return
name|configurationSource
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setConfigurationSource
parameter_list|(
name|Resource
name|configurationSource
parameter_list|)
block|{
name|this
operator|.
name|configurationSource
operator|=
name|configurationSource
expr_stmt|;
block|}
block|}
end_class

end_unit

