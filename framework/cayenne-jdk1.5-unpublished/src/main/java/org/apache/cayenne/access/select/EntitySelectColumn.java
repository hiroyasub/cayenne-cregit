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
name|access
operator|.
name|select
package|;
end_package

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
name|List
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
name|types
operator|.
name|ExtendedType
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
name|DbRelationship
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|EntitySelectColumn
implements|implements
name|SelectColumn
block|{
specifier|private
name|int
name|jdbcType
decl_stmt|;
specifier|private
name|String
name|dataRowKey
decl_stmt|;
specifier|private
name|ExtendedType
name|converter
decl_stmt|;
specifier|private
name|String
name|columnName
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|path
decl_stmt|;
specifier|public
name|int
name|getJdbcType
parameter_list|()
block|{
return|return
name|jdbcType
return|;
block|}
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|unionSegmentIndex
parameter_list|,
name|String
name|tableAlias
parameter_list|)
block|{
if|if
condition|(
name|tableAlias
operator|==
literal|null
operator|||
name|tableAlias
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|columnName
return|;
block|}
return|return
name|tableAlias
operator|+
literal|'.'
operator|+
name|columnName
return|;
block|}
specifier|public
name|String
name|getDataRowKey
parameter_list|()
block|{
return|return
name|dataRowKey
return|;
block|}
specifier|public
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getPath
parameter_list|(
name|int
name|unionSegmentIndex
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|path
return|;
block|}
name|ExtendedType
name|getConverter
parameter_list|()
block|{
return|return
name|converter
return|;
block|}
name|void
name|setJdbcType
parameter_list|(
name|int
name|jdbcType
parameter_list|)
block|{
name|this
operator|.
name|jdbcType
operator|=
name|jdbcType
expr_stmt|;
block|}
name|void
name|setPath
parameter_list|(
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
name|void
name|setColumnName
parameter_list|(
name|String
name|columnName
parameter_list|)
block|{
name|this
operator|.
name|columnName
operator|=
name|columnName
expr_stmt|;
block|}
name|void
name|setDataRowKey
parameter_list|(
name|String
name|dataRowKey
parameter_list|)
block|{
name|this
operator|.
name|dataRowKey
operator|=
name|dataRowKey
expr_stmt|;
block|}
name|void
name|setConverter
parameter_list|(
name|ExtendedType
name|converter
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|converter
expr_stmt|;
block|}
block|}
end_class

end_unit

