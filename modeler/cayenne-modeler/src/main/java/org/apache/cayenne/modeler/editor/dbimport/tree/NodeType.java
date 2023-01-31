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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
operator|.
name|tree
package|;
end_package

begin_comment
comment|/**  * @since 5.0  */
end_comment

begin_enum
specifier|public
enum|enum
name|NodeType
block|{
name|UNKNOWN
block|,
name|CATALOG
block|,
name|SCHEMA
block|,
name|INCLUDE_TABLE
block|,
name|EXCLUDE_TABLE
block|,
name|INCLUDE_COLUMN
block|,
name|EXCLUDE_COLUMN
block|,
name|INCLUDE_PROCEDURE
block|,
name|EXCLUDE_PROCEDURE
block|}
end_enum

end_unit

