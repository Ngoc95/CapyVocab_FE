//package com.example.capyvocab_fe.user.profile.data.repository
//
//import android.net.Uri
//import arrow.core.Either
//import arrow.core.Either.Companion.catch
//import com.example.capyvocab_fe.MyApplication
//import com.example.capyvocab_fe.admin.user.domain.model.User
//import com.example.capyvocab_fe.core.error.AppFailure
//import com.example.capyvocab_fe.core.error.toAppFailure
//import com.example.capyvocab_fe.user.community.domain.model.Post
//import com.example.capyvocab_fe.user.profile.data.remote.UserProfileApi
//import com.example.capyvocab_fe.user.profile.data.remote.model.DeleteUserResponse
//import com.example.capyvocab_fe.user.profile.data.remote.model.LogoutResponse
//import com.example.capyvocab_fe.user.profile.data.remote.model.UpdateUserRequest
//import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
//import com.example.capyvocab_fe.user.profile.domain.repository.UserProfileRepository
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.IOException
//import javax.inject.Inject
//
//class UserProfileRepositoryImpl @Inject constructor(
//    private val api: UserProfileApi
//) : UserProfileRepository {
//    override suspend fun getPostByUser(page: Int): Either<AppFailure, List<Post>> {
//        return Either.catch {
//            val response = api.getPostByUser(page = 1)
//            response.metaData.posts
//        }.mapLeft { it.toAppFailure() }
//    }
//
//    override suspend fun updateUser(user: ProfileUser): Either<AppFailure, ProfileUser> {
//        return Either.catch {
//            val request = UpdateUserRequest(
//                username =  user.username,
//                email = user.email,
//                avatar = user.avatar,
//                status = user.status
//            )
//            api.updateUser(id = user.id, updateRequest =  request).metaData
//        }.mapLeft { it.toAppFailure() }
//    }
//
//    override suspend fun getUserInfor(id: Int): Either<AppFailure, ProfileUser> {
//        return Either.catch {
//            api.getUserInfor(id).metaData
//        }.mapLeft { it.toAppFailure() }
//    }
//
//    override suspend fun uploadImage(uri: Uri): Either<AppFailure, String> {
//        return catch {
//            val contentResolver = MyApplication.instance.contentResolver
//            val inputStream =
//                contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
//            val fileName = "${System.currentTimeMillis()}.jpg"
//            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
//
//            val multipart = MultipartBody.Part.createFormData("AVATAR", fileName, requestBody)
//            val typePart = "AVATAR".toRequestBody("text/plain".toMediaType())
//
//            val response = api.uploadImage(typePart, multipart)
//            response.metaData.firstOrNull()?.destination
//                ?: throw IOException("Không nhận được URL ảnh")
//        }.mapLeft { it.toAppFailure() }
//    }
//
//
//    override suspend fun logout(user: ProfileUser): Either<AppFailure, LogoutResponse> {
//        return Either.catch{
//            api.logout().metaData
//        }.mapLeft { it.toAppFailure() }
//    }
//
//    override suspend fun deleteUser(user: ProfileUser): Either<AppFailure, DeleteUserResponse> {
//        return Either.catch{
//            val id = user.id;
//            api.deleteUserById(id).metaData
//        }.mapLeft { it.toAppFailure() }
//    }
//}