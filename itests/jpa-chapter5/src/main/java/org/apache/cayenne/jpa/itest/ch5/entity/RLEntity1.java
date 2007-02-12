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
name|itest
operator|.
name|ch5
operator|.
name|entity
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|RLEntity1
block|{
annotation|@
name|Id
specifier|protected
name|int
name|id
decl_stmt|;
specifier|protected
name|String
name|property1
decl_stmt|;
specifier|public
name|int
name|idField
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|updateIdField
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getProperty1
parameter_list|()
block|{
return|return
name|property1
return|;
block|}
specifier|public
name|void
name|setProperty1
parameter_list|(
name|String
name|property1
parameter_list|)
block|{
name|this
operator|.
name|property1
operator|=
name|property1
expr_stmt|;
block|}
block|}
end_class

end_unit

