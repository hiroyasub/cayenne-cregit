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
name|modeler
operator|.
name|graph
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
name|map
operator|.
name|Attribute
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
name|Entity
import|;
end_import

begin_comment
comment|/**  * Descriptor of ObjEntity Cell  */
end_comment

begin_class
class|class
name|ObjEntityCellMetadata
extends|extends
name|EntityCellMetadata
block|{
name|ObjEntityCellMetadata
parameter_list|(
name|GraphBuilder
name|builder
parameter_list|,
name|String
name|entityName
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|,
name|entityName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Entity
name|fetchEntity
parameter_list|()
block|{
return|return
name|builder
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isPrimary
parameter_list|(
name|Attribute
name|attr
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

