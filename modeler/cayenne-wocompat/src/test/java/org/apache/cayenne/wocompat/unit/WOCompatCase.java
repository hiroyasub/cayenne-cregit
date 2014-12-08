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
name|wocompat
operator|.
name|unit
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
name|test
operator|.
name|file
operator|.
name|FileUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_class
specifier|public
class|class
name|WOCompatCase
block|{
specifier|protected
name|File
name|setupTestDirectory
parameter_list|(
name|String
name|subfolder
parameter_list|)
block|{
name|String
name|classPath
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|location
init|=
literal|"target/testrun/"
operator|+
name|classPath
operator|+
literal|"/"
operator|+
name|subfolder
decl_stmt|;
name|File
name|testDirectory
init|=
operator|new
name|File
argument_list|(
name|location
argument_list|)
decl_stmt|;
comment|// delete old tests
if|if
condition|(
name|testDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|FileUtil
operator|.
name|delete
argument_list|(
name|location
argument_list|,
literal|true
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error deleting test directory '%s'"
argument_list|,
name|location
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|testDirectory
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating test directory '%s'"
argument_list|,
name|location
argument_list|)
throw|;
block|}
return|return
name|testDirectory
return|;
block|}
block|}
end_class

end_unit

