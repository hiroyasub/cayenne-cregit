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
name|validation
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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * An exception thrown on unsuccessful validation.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ValidationException
extends|extends
name|CayenneRuntimeException
block|{
specifier|private
name|ValidationResult
name|result
decl_stmt|;
specifier|public
name|ValidationException
parameter_list|(
name|String
name|messageFormat
parameter_list|,
name|Object
modifier|...
name|messageArgs
parameter_list|)
block|{
name|super
argument_list|(
name|messageFormat
argument_list|,
name|messageArgs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ValidationException
parameter_list|(
name|ValidationResult
name|result
parameter_list|)
block|{
comment|// escape percent signs so they aren't interpreted as format specifiers when String.format is called later.
name|this
argument_list|(
literal|"Validation failures: "
operator|+
name|result
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|"%"
argument_list|,
literal|"%%"
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ValidationException
parameter_list|(
name|String
name|messageFormat
parameter_list|,
name|ValidationResult
name|result
parameter_list|,
name|Object
modifier|...
name|messageArgs
parameter_list|)
block|{
name|super
argument_list|(
name|messageFormat
argument_list|,
name|messageArgs
argument_list|)
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
specifier|public
name|ValidationResult
name|getValidationResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
operator|+
name|this
operator|.
name|result
return|;
block|}
block|}
end_class

end_unit

