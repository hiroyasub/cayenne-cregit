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
name|instrument
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|instrument
operator|.
name|Instrumentation
import|;
end_import

begin_comment
comment|/**  * An interface for pluggable instrumentation agent.  *   * @see InstrumentUtil#INSTRUMENTATION_FACTORY_PROPERTY  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|InstrumentationFactory
block|{
comment|/**      * Returns an {@link Instrumentation} instance for the current JVM or null if it is      * not availble or not known to this factory.      */
name|Instrumentation
name|getInstrumentation
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

