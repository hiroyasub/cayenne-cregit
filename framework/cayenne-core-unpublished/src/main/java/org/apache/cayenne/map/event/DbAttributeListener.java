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
name|map
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
import|;
end_import

begin_comment
comment|/** For managing the changes in the DbAttribute. */
end_comment

begin_interface
specifier|public
interface|interface
name|DbAttributeListener
extends|extends
name|EventListener
block|{
comment|/** Attribute property changed. */
specifier|public
name|void
name|dbAttributeChanged
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
function_decl|;
comment|/** New attribute has been created/added.*/
specifier|public
name|void
name|dbAttributeAdded
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
function_decl|;
comment|/** Attribute has been removed.*/
specifier|public
name|void
name|dbAttributeRemoved
parameter_list|(
name|AttributeEvent
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
