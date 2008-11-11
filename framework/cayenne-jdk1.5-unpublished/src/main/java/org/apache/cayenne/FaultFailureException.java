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
package|;
end_package

begin_comment
comment|/**  * A runtime exception thrown when<code>DataObject.resolveFault()</code> finds that no  * matching row exists in the database for an<code>ObjectId</code>.  *   */
end_comment

begin_class
specifier|public
class|class
name|FaultFailureException
extends|extends
name|CayenneRuntimeException
block|{
comment|/**      * Creates new FaultFailureException without detail message.      */
specifier|public
name|FaultFailureException
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructs an FaultFailureException with the specified detail message.      *       * @param msg the detail message.      */
specifier|public
name|FaultFailureException
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
comment|/**      * Constructs an FaultFailureException that wraps a<code>Throwable</code> thrown      * elsewhere.      */
specifier|public
name|FaultFailureException
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
specifier|public
name|FaultFailureException
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
block|}
end_class

end_unit

