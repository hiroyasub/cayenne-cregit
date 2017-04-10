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

begin_comment
comment|/**  * An enum of possible operations that can be audited.  *   * @since 3.1  * @deprecated since 4.0, use {@link org.apache.cayenne.lifecycle.postcommit.PostCommitFilter}  * @see org.apache.cayenne.lifecycle.changemap.ObjectChangeType  */
end_comment

begin_enum
annotation|@
name|Deprecated
specifier|public
enum|enum
name|AuditableOperation
block|{
name|INSERT
block|,
name|UPDATE
block|,
name|DELETE
block|; }
end_enum

end_unit

