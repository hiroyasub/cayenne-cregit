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
name|db
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
name|lifecycle
operator|.
name|audit
operator|.
name|AuditableChild
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
name|lifecycle
operator|.
name|db
operator|.
name|auto
operator|.
name|_AuditableChildUuid
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
name|lifecycle
operator|.
name|relationship
operator|.
name|ObjectIdRelationship
import|;
end_import

begin_class
annotation|@
name|ObjectIdRelationship
argument_list|(
name|_AuditableChildUuid
operator|.
name|UUID_PROPERTY
argument_list|)
annotation|@
name|AuditableChild
argument_list|(
name|objectIdRelationship
operator|=
name|_AuditableChildUuid
operator|.
name|UUID_PROPERTY
argument_list|)
specifier|public
class|class
name|AuditableChildUuid
extends|extends
name|_AuditableChildUuid
block|{ }
end_class

end_unit

