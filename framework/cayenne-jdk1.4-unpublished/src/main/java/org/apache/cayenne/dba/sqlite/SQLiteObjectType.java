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
name|dba
operator|.
name|sqlite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|access
operator|.
name|types
operator|.
name|AbstractType
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
name|access
operator|.
name|types
operator|.
name|DefaultType
import|;
end_import

begin_comment
comment|/**  * This ExtendedType is used by SQLite as often the types of columns of the result sets  * can't be determined on the fly, and {@link DefaultType} for Object class throws on  * NULLs.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|SQLiteObjectType
extends|extends
name|AbstractType
block|{
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|rs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|rs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
end_class

end_unit

