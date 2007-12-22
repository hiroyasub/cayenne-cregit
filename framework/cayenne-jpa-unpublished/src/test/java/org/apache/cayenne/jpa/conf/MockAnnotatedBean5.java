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
name|conf
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Basic
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectId
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|MockAnnotatedBean5
block|{
annotation|@
name|Id
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
literal|35
return|;
block|}
specifier|public
name|ObjectId
name|getObjectId
parameter_list|()
block|{
return|return
operator|new
name|ObjectId
argument_list|(
literal|"MockAnnotatedBean5"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setObjectId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
block|}
annotation|@
name|Basic
specifier|public
name|int
name|getAttribute1
parameter_list|()
block|{
return|return
literal|66
return|;
block|}
specifier|public
name|void
name|setAttribute1
parameter_list|(
name|int
name|a
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

