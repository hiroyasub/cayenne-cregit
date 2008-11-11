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
name|remote
operator|.
name|hessian
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|CayenneRuntimeException
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
name|DataRow
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|AbstractHessianInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|AbstractMapDeserializer
import|;
end_import

begin_comment
comment|/**  * Client side deserilaizer of DataRows.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DataRowDeserializer
extends|extends
name|AbstractMapDeserializer
block|{
specifier|protected
name|Field
name|versionField
decl_stmt|;
name|DataRowDeserializer
parameter_list|()
block|{
try|try
block|{
name|versionField
operator|=
name|DataRow
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error building deserializer for DataRow"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|versionField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|DataRow
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readMap
parameter_list|(
name|AbstractHessianInput
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|size
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
name|size
argument_list|)
decl_stmt|;
try|try
block|{
name|versionField
operator|.
name|set
argument_list|(
name|row
argument_list|,
operator|new
name|Long
argument_list|(
name|in
operator|.
name|readLong
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading 'version' field"
argument_list|)
throw|;
block|}
name|row
operator|.
name|setReplacesVersion
argument_list|(
name|in
operator|.
name|readLong
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|addRef
argument_list|(
name|row
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|in
operator|.
name|isEnd
argument_list|()
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
argument_list|,
name|in
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|readEnd
argument_list|()
expr_stmt|;
return|return
name|row
return|;
block|}
block|}
end_class

end_unit

