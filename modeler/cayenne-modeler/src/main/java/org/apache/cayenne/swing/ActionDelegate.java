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
name|swing
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

begin_comment
comment|/**  * An implementation of BindingDelegate that invokes a no-argument context action on every  * model update.  *   */
end_comment

begin_class
specifier|public
class|class
name|ActionDelegate
implements|implements
name|BindingDelegate
block|{
specifier|protected
name|BindingExpression
name|expression
decl_stmt|;
specifier|public
name|ActionDelegate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
operator|new
name|BindingExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|modelUpdated
parameter_list|(
name|ObjectBinding
name|binding
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
comment|// TODO: might add new and old value as variables...
name|expression
operator|.
name|getValue
argument_list|(
name|binding
operator|.
name|getContext
argument_list|()
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

