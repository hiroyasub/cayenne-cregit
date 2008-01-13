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
name|project
operator|.
name|validator
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
name|project
operator|.
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * ValidationInfo encapsulates information about a single node validation  * on the project tree.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ValidationInfo
block|{
specifier|public
specifier|static
specifier|final
name|int
name|VALID
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|WARNING
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ERROR
init|=
literal|2
decl_stmt|;
specifier|protected
name|ProjectPath
name|path
decl_stmt|;
specifier|protected
name|String
name|message
decl_stmt|;
specifier|protected
name|int
name|severity
decl_stmt|;
comment|/**      * Constructor for ValidationInfo.       */
specifier|public
name|ValidationInfo
parameter_list|(
name|int
name|severity
parameter_list|,
name|String
name|message
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
name|this
operator|.
name|severity
operator|=
name|severity
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|Object
name|getValidatedObject
parameter_list|()
block|{
return|return
name|path
operator|.
name|getObject
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getValidatedObjectParent
parameter_list|()
block|{
return|return
name|path
operator|.
name|getObjectParent
argument_list|()
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
name|getMessage
argument_list|()
return|;
block|}
comment|/**      * Returns the message.      * @return String      */
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**      * Returns the severity.      * @return int      */
specifier|public
name|int
name|getSeverity
parameter_list|()
block|{
return|return
name|severity
return|;
block|}
comment|/**      * Returns the ProjectPath object identifing a location      * described by this ValidationInfo.      */
specifier|public
name|ProjectPath
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

