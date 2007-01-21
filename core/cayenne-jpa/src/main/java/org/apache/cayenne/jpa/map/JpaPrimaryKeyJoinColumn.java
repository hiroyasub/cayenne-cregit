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
operator|.
name|map
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PrimaryKeyJoinColumn
import|;
end_import

begin_class
specifier|public
class|class
name|JpaPrimaryKeyJoinColumn
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|referencedColumnName
decl_stmt|;
specifier|protected
name|String
name|columnDefinition
decl_stmt|;
specifier|public
name|JpaPrimaryKeyJoinColumn
parameter_list|()
block|{
block|}
specifier|public
name|JpaPrimaryKeyJoinColumn
parameter_list|(
name|PrimaryKeyJoinColumn
name|annotation
parameter_list|)
block|{
name|name
operator|=
name|annotation
operator|.
name|name
argument_list|()
expr_stmt|;
name|referencedColumnName
operator|=
name|annotation
operator|.
name|referencedColumnName
argument_list|()
expr_stmt|;
name|columnDefinition
operator|=
name|annotation
operator|.
name|columnDefinition
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns columnDefinition property value.      *<h3>Specification Docs</h3>      *<p>      *<b>Description:</b> (Optional) A SQL fragment that is used when generating DDL for      * the column.      *</p>      *<p>      *<b>Default:</b> generated SQL to create column of the inferred type.      *</p>      */
specifier|public
name|String
name|getColumnDefinition
parameter_list|()
block|{
return|return
name|columnDefinition
return|;
block|}
specifier|public
name|void
name|setColumnDefinition
parameter_list|(
name|String
name|columnDefinition
parameter_list|)
block|{
name|this
operator|.
name|columnDefinition
operator|=
name|columnDefinition
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
name|String
name|getReferencedColumnName
parameter_list|()
block|{
return|return
name|referencedColumnName
return|;
block|}
specifier|public
name|void
name|setReferencedColumnName
parameter_list|(
name|String
name|referencedColumnName
parameter_list|)
block|{
name|this
operator|.
name|referencedColumnName
operator|=
name|referencedColumnName
expr_stmt|;
block|}
block|}
end_class

end_unit

