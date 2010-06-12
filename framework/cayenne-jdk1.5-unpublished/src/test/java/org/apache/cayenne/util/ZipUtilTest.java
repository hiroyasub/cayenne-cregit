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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_comment
comment|/**  * @deprecated since 3.1  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|ZipUtilTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testUnzip
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|jarResource
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/cayenne/util/jar-test.jar"
argument_list|)
decl_stmt|;
name|File
name|jarCopy
init|=
operator|new
name|File
argument_list|(
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
argument_list|,
literal|"jar-test.jar"
argument_list|)
decl_stmt|;
name|Util
operator|.
name|copy
argument_list|(
name|jarResource
argument_list|,
name|jarCopy
argument_list|)
expr_stmt|;
name|File
name|unjarDir
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
decl_stmt|;
name|File
name|unjarRootDir
init|=
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"jar-test"
argument_list|)
decl_stmt|;
name|File
name|manifest
init|=
operator|new
name|File
argument_list|(
name|unjarRootDir
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"META-INF"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"MANIFEST.MF"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|unjarRootDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|manifest
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// try unzipping the JAR
name|ZipUtil
operator|.
name|unzip
argument_list|(
name|jarCopy
argument_list|,
name|unjarDir
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|unjarRootDir
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|unjarRootDir
argument_list|,
literal|"jar-test1.txt"
argument_list|)
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|unjarRootDir
argument_list|,
literal|"jar-test2.txt"
argument_list|)
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|manifest
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Util
operator|.
name|delete
argument_list|(
name|unjarRootDir
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Util
operator|.
name|delete
argument_list|(
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"META-INF"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testZip
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|jarResource
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/cayenne/util/jar-test.jar"
argument_list|)
decl_stmt|;
name|File
name|jarCopy
init|=
operator|new
name|File
argument_list|(
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
argument_list|,
literal|"jar-test.jar"
argument_list|)
decl_stmt|;
name|Util
operator|.
name|copy
argument_list|(
name|jarResource
argument_list|,
name|jarCopy
argument_list|)
expr_stmt|;
name|File
name|unjarDir
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
decl_stmt|;
name|File
name|unjarRootDir
init|=
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"jar-test"
argument_list|)
decl_stmt|;
name|File
name|newJarFile
init|=
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"new-jar.jar"
argument_list|)
decl_stmt|;
try|try
block|{
comment|// unzip existing jar and recreate
name|assertFalse
argument_list|(
name|unjarRootDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|ZipUtil
operator|.
name|unzip
argument_list|(
name|jarCopy
argument_list|,
name|unjarDir
argument_list|)
expr_stmt|;
name|ZipUtil
operator|.
name|zip
argument_list|(
name|newJarFile
argument_list|,
name|unjarDir
argument_list|,
operator|new
name|File
index|[]
block|{
name|unjarRootDir
block|,
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"META-INF"
argument_list|)
block|}
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|newJarFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
comment|// can't compare length, since different algorithms may have been used
comment|// assertEquals(jar.length(), newJarFile.length());
comment|// try unzipping it again
name|Util
operator|.
name|delete
argument_list|(
name|unjarRootDir
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Util
operator|.
name|delete
argument_list|(
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"META-INF"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ZipUtil
operator|.
name|unzip
argument_list|(
name|newJarFile
argument_list|,
name|unjarDir
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Util
operator|.
name|delete
argument_list|(
name|unjarRootDir
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Util
operator|.
name|delete
argument_list|(
operator|new
name|File
argument_list|(
name|unjarDir
argument_list|,
literal|"META-INF"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|newJarFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

