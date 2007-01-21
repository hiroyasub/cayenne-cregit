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
name|exp
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
comment|/**   * RuntimeException subclass thrown in cases of errors during   * expressions creation/parsing.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ExpressionException
extends|extends
name|CayenneRuntimeException
block|{
specifier|protected
name|String
name|expressionString
decl_stmt|;
comment|/**      * Constructor for ExpressionException.      */
specifier|public
name|ExpressionException
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for ExpressionException.      * @param msg      */
specifier|public
name|ExpressionException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ExpressionException.      * @param th      */
specifier|public
name|ExpressionException
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|super
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ExpressionException.      * @param msg      * @param th      */
specifier|public
name|ExpressionException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ExpressionException.      *       * @since 1.1      */
specifier|public
name|ExpressionException
parameter_list|(
name|String
name|msg
parameter_list|,
name|String
name|expressionString
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|this
operator|.
name|expressionString
operator|=
name|expressionString
expr_stmt|;
block|}
specifier|public
name|String
name|getExpressionString
parameter_list|()
block|{
return|return
name|expressionString
return|;
block|}
block|}
end_class

end_unit

