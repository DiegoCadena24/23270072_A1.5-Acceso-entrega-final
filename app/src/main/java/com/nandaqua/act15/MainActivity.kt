package com.nandaqua.act15

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class UserData(
    val controlNumber: String,
    val name: String,
    val username: String,
    val career: String,
    val institution: String,
    val semester: String
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {

    var isLogged by remember {
        mutableStateOf(false)
    }

    val sampleUser = UserData(
        controlNumber = "23270072",
        name = "Diego Eduardo Vicente Cadena",
        username = "23270072",
        career = "Ingeniería en Sistemas Computacionales",
        institution = "Instituto Tecnológico de Tuxtla Gutiérrez",
        semester = "Octavo Semestre"
    )

    if (isLogged) {

        ProfileScreen(
            user = sampleUser,
            onLogout = {
                isLogged = false
            }
        )

    } else {

        LoginScreen(
            sampleUser = sampleUser,
            onLoginSuccess = {
                isLogged = true
            }
        )
    }
}

@Composable
fun LoginScreen(
    sampleUser: UserData,
    onLoginSuccess: () -> Unit
) {

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TecLogoComponent()

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Instituto Tecnológico de Tuxtla Gutiérrez",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = ""
            },
            label = {
                Text("Número de Control")
            },
            leadingIcon = {
                Text(
                    text = "👤",
                    fontSize = 18.sp
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = {
                Text("Contraseña")
            },
            leadingIcon = {
                Text(
                    text = "🔒",
                    fontSize = 18.sp
                )
            },
            singleLine = true,
            visualTransformation =
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            trailingIcon = {

                IconButton(
                    onClick = {
                        isPasswordVisible =
                            !isPasswordVisible
                    }
                ) {

                    Text(
                        text = if (isPasswordVisible) {
                            "👁️"
                        } else {
                            "🙈"
                        },
                        fontSize = 16.sp
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                if (
                    username == sampleUser.controlNumber &&
                    password == "Cadena2485"
                ) {

                    onLoginSuccess()

                } else {

                    errorMessage =
                        "Número de control o contraseña incorrectos"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {

            Text(
                text = "Iniciar Sesión",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProfileScreen(
    user: UserData,
    onLogout: () -> Unit
) {

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {
                selectedImageUri = uri
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        UserAvatarComponent(
            name = user.name,
            imageUri = selectedImageUri,
            onClick = {
                galleryLauncher.launch("image/*")
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Toca la foto para cambiarla",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                UserDetailItem(
                    label = "Número de Control",
                    value = user.controlNumber
                )

                HorizontalDivider()

                UserDetailItem(
                    label = "Nombre Completo",
                    value = user.name
                )

                HorizontalDivider()

                UserDetailItem(
                    label = "Nombre de Usuario",
                    value = user.username
                )

                HorizontalDivider()

                UserDetailItem(
                    label = "Carrera",
                    value = user.career
                )

                HorizontalDivider()

                UserDetailItem(
                    label = "Institución",
                    value = user.institution
                )

                HorizontalDivider()

                UserDetailItem(
                    label = "Semestre",
                    value = user.semester
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            Text(
                text = "Cerrar Sesión"
            )
        }
    }
}

@Composable
fun UserDetailItem(
    label: String,
    value: String
) {

    Column {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TecLogoComponent() {

    Box(
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {

        val primaryColor =
            MaterialTheme.colorScheme.primary

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val w = size.width
            val h = size.height

            drawCircle(
                color = primaryColor,
                radius = w / 2 * 0.9f
            )

            drawCircle(
                color = Color.White,
                radius = w / 2 * 0.7f
            )

            val path = Path().apply {

                moveTo(
                    w * 0.3f,
                    h * 0.7f
                )

                lineTo(
                    w * 0.5f,
                    h * 0.25f
                )

                lineTo(
                    w * 0.7f,
                    h * 0.7f
                )

                close()
            }

            drawPath(
                path = path,
                color = primaryColor
            )
        }

        Text(
            text = "TEC",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp
        )
    }
}

@Composable
fun UserAvatarComponent(
    name: String,
    imageUri: Uri?,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    val initials = name
        .split(" ")
        .take(2)
        .mapNotNull {
            it.firstOrNull()?.uppercase()
        }
        .joinToString("")

    var bitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    LaunchedEffect(imageUri) {

        if (imageUri != null) {

            bitmap = withContext(Dispatchers.IO) {

                try {

                    context.contentResolver
                        .openInputStream(imageUri)
                        ?.use { inputStream ->
                            BitmapFactory.decodeStream(
                                inputStream
                            )
                        }

                } catch (e: Exception) {

                    null
                }
            }
        } else {

            bitmap = null
        }
    }

    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.primaryContainer
            )
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        if (bitmap != null) {

            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

        } else {

            Text(
                text = initials,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}