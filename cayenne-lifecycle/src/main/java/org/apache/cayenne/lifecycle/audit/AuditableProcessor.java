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
name|lifecycle
operator|.
name|audit
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
name|Persistent
import|;
end_import

begin_comment
comment|/**  * A superclass of application specific handlers of the {@link Auditable}  * annotation, that provides basic needed callbacks.  *   * @since 3.1  * @deprecated since 4.0, use {@link org.apache.cayenne.lifecycle.postcommit.PostCommitFilter}  */
end_comment

begin_interface
specifier|public
interface|interface
name|AuditableProcessor
block|{
comment|/**      * A method called by {@link AuditableFilter} that should audit records as      * appropriate in a given application. Implementors may insert audit records      * in DB, log a message, etc.      *       * @param object      *            the root auditable object. This is an object that is either      *            annotated with {@link Auditable} or pointed to by another      *            object annotated with {@link AuditableChild}.      * @param operation      *            a type of object change.      */
name|void
name|audit
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|AuditableOperation
name|operation
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

