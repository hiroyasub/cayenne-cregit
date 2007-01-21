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
name|ch2
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
name|FieldPersistenceEntity
block|{
specifier|public
specifier|static
specifier|final
name|String
name|INITIAL_VALUE
init|=
literal|"Init Value"
decl_stmt|;
annotation|@
name|Id
specifier|protected
name|int
name|id
decl_stmt|;
specifier|protected
name|String
name|property1
init|=
name|INITIAL_VALUE
decl_stmt|;
specifier|public
name|int
name|getProperty1
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getter is not supposed to be called in case of field based persistence"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setProperty1
parameter_list|(
name|int
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"setter is not supposed to be called in case of field based persistence"
argument_list|)
throw|;
block|}
specifier|public
name|int
name|idField
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

