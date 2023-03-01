begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|value
operator|.
name|json
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|experimental
operator|.
name|runners
operator|.
name|Enclosed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThrows
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|Enclosed
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JsonUtilsTest
block|{
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
specifier|static
class|class
name|CompareTest
block|{
annotation|@
name|Parameterized
operator|.
name|Parameters
argument_list|(
name|name
operator|=
literal|" {0} eq {1} "
argument_list|)
specifier|public
specifier|static
name|Object
index|[]
index|[]
name|data
parameter_list|()
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
block|{
literal|"[]"
block|,
literal|"[]"
block|,
literal|true
block|}
block|,
block|{
literal|"{}"
block|,
literal|"{}"
block|,
literal|true
block|}
block|,
block|{
literal|"[]"
block|,
literal|"{}"
block|,
literal|false
block|}
block|,
block|{
literal|"123"
block|,
literal|"123"
block|,
literal|true
block|}
block|,
block|{
literal|"123"
block|,
literal|"124"
block|,
literal|false
block|}
block|,
block|{
literal|"null"
block|,
literal|"null"
block|,
literal|true
block|}
block|,
block|{
literal|"true"
block|,
literal|"true"
block|,
literal|true
block|}
block|,
block|{
literal|"true"
block|,
literal|"false"
block|,
literal|false
block|}
block|,
block|{
literal|"\"123\""
block|,
literal|"\"123\""
block|,
literal|true
block|}
block|,
block|{
literal|"123"
block|,
literal|"\"123\""
block|,
literal|false
block|}
block|,
block|{
literal|"[1,2,3]"
block|,
literal|"[1, 2, 3]"
block|,
literal|true
block|}
block|,
block|{
literal|"[1,2,3]"
block|,
literal|"[1,2,3,4]"
block|,
literal|false
block|}
block|,
block|{
literal|"[1,2,3]"
block|,
literal|"[1,2]"
block|,
literal|false
block|}
block|,
block|{
literal|"[1,2,3]"
block|,
literal|"[1,2,4]"
block|,
literal|false
block|}
block|,
block|{
literal|"{\"abc\":123,\"def\":321}"
block|,
literal|" {\"def\" :  321 , \n\t\"abc\" :\t123 }"
block|,
literal|true
block|}
block|,
block|{
literal|"{\"abc\":123}"
block|,
literal|" {\"abc\" :  124 }"
block|,
literal|false
block|}
block|}
return|;
block|}
annotation|@
name|Parameterized
operator|.
name|Parameter
specifier|public
name|String
name|jsonStringA
decl_stmt|;
annotation|@
name|Parameterized
operator|.
name|Parameter
argument_list|(
literal|1
argument_list|)
specifier|public
name|String
name|jsonStringB
decl_stmt|;
annotation|@
name|Parameterized
operator|.
name|Parameter
argument_list|(
literal|2
argument_list|)
specifier|public
name|boolean
name|areEquals
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|compare
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|areEquals
argument_list|,
name|JsonUtils
operator|.
name|compare
argument_list|(
name|jsonStringA
argument_list|,
name|jsonStringB
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
specifier|static
class|class
name|NormalizeTest
block|{
annotation|@
name|Parameterized
operator|.
name|Parameters
argument_list|(
name|name
operator|=
literal|" {0} "
argument_list|)
specifier|public
specifier|static
name|Object
index|[]
index|[]
name|data
parameter_list|()
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
block|{
literal|"[]"
block|,
literal|"[]"
block|,
literal|null
block|}
block|,
block|{
literal|"{}"
block|,
literal|"{}"
block|,
literal|null
block|}
block|,
block|{
literal|"true"
block|,
literal|"true"
block|,
literal|null
block|}
block|,
block|{
literal|"null"
block|,
literal|"null"
block|,
literal|null
block|}
block|,
block|{
literal|"false"
block|,
literal|"false"
block|,
literal|null
block|}
block|,
block|{
literal|"123"
block|,
literal|"123"
block|,
literal|null
block|}
block|,
block|{
literal|"-10.24e3"
block|,
literal|"-10.24e3"
block|,
literal|null
block|}
block|,
block|{
literal|"\"abc\\\"def\""
block|,
literal|"\"abc\\\"def\""
block|,
literal|null
block|}
block|,
block|{
literal|"[1, 2.0, -0.3e3, false, null, true]"
block|,
literal|"[1 ,  2.0  ,-0.3e3, false,\nnull,\ttrue]"
block|,
literal|null
block|}
block|,
block|{
literal|"{\"abc\": 321, \"def\": true, \"ghi\": \"jkl\"}"
block|,
literal|"{\"abc\":321,\n\"def\":true,\n\t\"ghi\":\"jkl\"}"
block|,
literal|null
block|}
block|,
block|{
literal|"{\"tags\": [\"ad\", \"irure\", \"anim\"], \"age\": 20}"
block|,
literal|"{\"tags\": [\"ad\",\n\"irure\", \"anim\"],\n\"age\": 20}"
block|,
literal|null
block|}
block|,
block|{
literal|"{\"objects\": [{\"id\": 1}, {\"id\": 2}]}"
block|,
literal|"{\"objects\":\n[\n{\n\"id\": 1\n},\n{\n\"id\": 2\n}\n]}"
block|,
literal|null
block|}
block|,
block|{
literal|"["
operator|+
literal|"{"
operator|+
literal|"\"_id\": \"63f218c8ae709e45c7b32c5f\", "
operator|+
literal|"\"index\": 0, "
operator|+
literal|"\"guid\": \"b3c2b147-9031-40ee-b2a9-fabbd7f5da81\", "
operator|+
literal|"\"isActive\": false, "
operator|+
literal|"\"balance\": \"$2,836.15\", "
operator|+
literal|"\"picture\": \"http://placehold.it/32x32\", "
operator|+
literal|"\"age\": 21, "
operator|+
literal|"\"eyeColor\": \"green\", "
operator|+
literal|"\"name\": \"Ratliff Martin\", "
operator|+
literal|"\"gender\": \"male\", "
operator|+
literal|"\"company\": \"PLASMOSIS\", "
operator|+
literal|"\"email\": \"ratliffmartin@plasmosis.com\", "
operator|+
literal|"\"phone\": \"+1 (897) 415-2945\", "
operator|+
literal|"\"address\": \"241 Foster Avenue, Outlook, New Jersey, 1479\", "
operator|+
literal|"\"about\": \"pariatur irure qui consequat excepteur laborum nulla\", "
operator|+
literal|"\"registered\": \"2018-05-18T08:04:15 -03:00\", "
operator|+
literal|"\"latitude\": -51.195497, "
operator|+
literal|"\"longitude\": 163.317807, "
operator|+
literal|"\"tags\": ["
operator|+
literal|"\"exercitation\", "
operator|+
literal|"\"nulla\", "
operator|+
literal|"\"labore\", "
operator|+
literal|"\"enim\", "
operator|+
literal|"\"ad\", "
operator|+
literal|"\"anim\", "
operator|+
literal|"\"excepteur\""
operator|+
literal|"], "
operator|+
literal|"\"friends\": ["
operator|+
literal|"{"
operator|+
literal|"\"id\": 0, "
operator|+
literal|"\"name\": \"Rowena Benson\""
operator|+
literal|"}, "
operator|+
literal|"{"
operator|+
literal|"\"id\": 1, "
operator|+
literal|"\"name\": \"Bird Mclaughlin\""
operator|+
literal|"}, "
operator|+
literal|"{"
operator|+
literal|"\"id\": 2, "
operator|+
literal|"\"name\": \"Mabel James\""
operator|+
literal|"}"
operator|+
literal|"], "
operator|+
literal|"\"greeting\": \"Hello, Ratliff Martin! You have 2 unread messages.\", "
operator|+
literal|"\"favoriteFruit\": \"strawberry\""
operator|+
literal|"}"
operator|+
literal|"]"
block|,
literal|"[\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f218c8ae709e45c7b32c5f\",\n"
operator|+
literal|"    \"index\": 0,\n"
operator|+
literal|"    \"guid\": \"b3c2b147-9031-40ee-b2a9-fabbd7f5da81\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,836.15\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 21,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Ratliff Martin\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"PLASMOSIS\",\n"
operator|+
literal|"    \"email\": \"ratliffmartin@plasmosis.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (897) 415-2945\",\n"
operator|+
literal|"    \"address\": \"241 Foster Avenue, Outlook, New Jersey, 1479\",\n"
operator|+
literal|"    \"about\": \"pariatur irure qui consequat excepteur laborum nulla\",\n"
operator|+
literal|"    \"registered\": \"2018-05-18T08:04:15 -03:00\",\n"
operator|+
literal|"    \"latitude\": -51.195497,\n"
operator|+
literal|"    \"longitude\": 163.317807,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"exercitation\",\n"
operator|+
literal|"      \"nulla\",\n"
operator|+
literal|"      \"labore\",\n"
operator|+
literal|"      \"enim\",\n"
operator|+
literal|"      \"ad\",\n"
operator|+
literal|"      \"anim\",\n"
operator|+
literal|"      \"excepteur\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Rowena Benson\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Bird Mclaughlin\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Mabel James\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Ratliff Martin! You have 2 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  }\n"
operator|+
literal|"]"
block|,
literal|null
block|}
block|,
block|{
literal|""
block|,
literal|""
block|,
name|MalformedJsonException
operator|.
name|class
block|}
block|,             }
return|;
block|}
annotation|@
name|Parameterized
operator|.
name|Parameter
specifier|public
name|String
name|expected
decl_stmt|;
annotation|@
name|Parameterized
operator|.
name|Parameter
argument_list|(
literal|1
argument_list|)
specifier|public
name|String
name|jsonString
decl_stmt|;
annotation|@
name|Parameterized
operator|.
name|Parameter
argument_list|(
literal|2
argument_list|)
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|throwable
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|normalize
parameter_list|()
block|{
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|JsonUtils
operator|.
name|normalize
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThrows
argument_list|(
name|throwable
argument_list|,
parameter_list|()
lambda|->
name|JsonUtils
operator|.
name|normalize
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

