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
name|testdo
operator|.
name|lifecycle_callbacks_order
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
name|testdo
operator|.
name|lifecycle_callbacks_order
operator|.
name|auto
operator|.
name|_Lifecycle
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_class
specifier|public
class|class
name|Lifecycle
extends|extends
name|_Lifecycle
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|StringBuilder
name|callbackBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|validateForInsert
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"validateForInsert;"
argument_list|)
expr_stmt|;
name|super
operator|.
name|validateForInsert
argument_list|(
name|validationResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateForUpdate
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"validateForUpdate;"
argument_list|)
expr_stmt|;
name|super
operator|.
name|validateForUpdate
argument_list|(
name|validationResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateForDelete
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"validateForDelete;"
argument_list|)
expr_stmt|;
name|super
operator|.
name|validateForDelete
argument_list|(
name|validationResult
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringBuilder
name|getCallbackBuffer
parameter_list|()
block|{
return|return
name|callbackBuffer
return|;
block|}
specifier|public
name|String
name|getCallbackBufferValueAndReset
parameter_list|()
block|{
name|String
name|v
init|=
name|callbackBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|callbackBuffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
return|return
name|v
return|;
block|}
block|}
end_class

end_unit

