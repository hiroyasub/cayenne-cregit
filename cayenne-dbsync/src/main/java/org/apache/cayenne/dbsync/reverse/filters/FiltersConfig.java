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
name|filters
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

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|FiltersConfig
block|{
specifier|private
specifier|final
name|CatalogFilter
index|[]
name|catalogs
decl_stmt|;
specifier|public
name|FiltersConfig
parameter_list|(
name|CatalogFilter
modifier|...
name|catalogs
parameter_list|)
block|{
if|if
condition|(
name|catalogs
operator|==
literal|null
operator|||
name|catalogs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"catalogs("
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|catalogs
argument_list|)
operator|+
literal|") can't be null or empty"
argument_list|)
throw|;
block|}
name|this
operator|.
name|catalogs
operator|=
name|catalogs
expr_stmt|;
block|}
specifier|public
name|CatalogFilter
index|[]
name|getCatalogs
parameter_list|()
block|{
return|return
name|catalogs
return|;
block|}
specifier|public
name|PatternFilter
name|proceduresFilter
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|SchemaFilter
name|schemaFilter
init|=
name|getSchemaFilter
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|)
decl_stmt|;
return|return
name|schemaFilter
operator|==
literal|null
condition|?
literal|null
else|:
name|schemaFilter
operator|.
name|procedures
return|;
block|}
specifier|public
name|TableFilter
name|tableFilter
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|SchemaFilter
name|schemaFilter
init|=
name|getSchemaFilter
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|)
decl_stmt|;
return|return
name|schemaFilter
operator|==
literal|null
condition|?
literal|null
else|:
name|schemaFilter
operator|.
name|tables
return|;
block|}
specifier|protected
name|SchemaFilter
name|getSchemaFilter
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|CatalogFilter
name|catalogFilter
init|=
name|getCatalog
argument_list|(
name|catalog
argument_list|)
decl_stmt|;
if|if
condition|(
name|catalogFilter
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|catalogFilter
operator|.
name|getSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
specifier|protected
name|CatalogFilter
name|getCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
block|{
for|for
control|(
name|CatalogFilter
name|catalogFilter
range|:
name|catalogs
control|)
block|{
if|if
condition|(
name|catalogFilter
operator|.
name|name
operator|==
literal|null
operator|||
name|catalogFilter
operator|.
name|name
operator|.
name|equals
argument_list|(
name|catalog
argument_list|)
condition|)
block|{
return|return
name|catalogFilter
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|CatalogFilter
name|catalog
range|:
name|catalogs
control|)
block|{
name|catalog
operator|.
name|toString
argument_list|(
name|builder
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|FiltersConfig
name|create
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|,
name|TableFilter
name|tableFilter
parameter_list|,
name|PatternFilter
name|procedures
parameter_list|)
block|{
return|return
operator|new
name|FiltersConfig
argument_list|(
operator|new
name|CatalogFilter
argument_list|(
name|catalog
argument_list|,
operator|new
name|SchemaFilter
argument_list|(
name|schema
argument_list|,
name|tableFilter
argument_list|,
name|procedures
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

