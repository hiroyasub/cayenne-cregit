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
name|access
operator|.
name|types
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
name|PreparedStatement
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

begin_comment
comment|/**  * Handles<code>java.lang.Byte</code> type mapping. Can be configured to recast  * java.lang.Byte to java.lang.Integer when binding values to PreparedStatement. This is a  * workaround for bugs in certain drivers. Drivers that are proven to have issues with  * byte values are Sybase and Oracle (Mac OS X only).  *   * @author Andrus Adamchik  * @since 1.0.3  */
end_comment

begin_class
specifier|public
class|class
name|ByteType
extends|extends
name|AbstractType
block|{
specifier|protected
name|boolean
name|widenBytes
decl_stmt|;
specifier|public
name|ByteType
parameter_list|(
name|boolean
name|widenBytes
parameter_list|)
block|{
name|this
operator|.
name|widenBytes
operator|=
name|widenBytes
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|byte
name|b
init|=
name|rs
operator|.
name|getByte
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|rs
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
operator|new
name|Byte
argument_list|(
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|st
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
name|byte
name|b
init|=
name|st
operator|.
name|getByte
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|st
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
name|Byte
operator|.
name|valueOf
argument_list|(
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|widenBytes
operator|&&
operator|(
name|val
operator|instanceof
name|Byte
operator|)
condition|)
block|{
name|val
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|Byte
operator|)
name|val
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setJdbcObject
argument_list|(
name|st
argument_list|,
name|val
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

