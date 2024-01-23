package com.joincoded.ComposeUI
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noura.composeui.R
import com.noura.composeui.ui.theme.ComposeUITheme


data class Question(val text: String, val isCorrect: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeUITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = colorScheme.background
                ) { TrueFalseGame() }
            }
        }
    }
}
@Composable
fun TrueFalseGame() {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuestionIndex = remember { mutableStateOf(0) }
    var AnsrCorrect by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var showAnswerOptionsRow by remember { mutableStateOf(true) }
    var userScore = remember { mutableStateOf(0) }
    var showResetButton by remember { mutableStateOf(false) }


    if (questions.isEmpty()) {
        questions = listOf(
            Question(stringResource("I work for an employee"), true),
            Question(stringResource("i am 27 years old"), false),
            Question(stringResource("i have two cats"), true),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = questions[currentQuestionIndex.value].text,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        if (AnsrCorrect && showFeedback ) {
            AnswerFeedback(stringResource(R.string.correct), colorScheme.secondary, true)
            //CorrectAnswerImage()
            Text(text = "Score: ${userScore.value}")

            Button(
                onClick = {
                    showFeedback = false
                    showAnswerOptionsRow = true
                    // Move to the next question or show the final score
                    if (currentQuestionIndex.value == questions.size - 1) {
                        showResetButton = true

                        AnsrCorrect = false
                    }

                    if (currentQuestionIndex.value < questions.size - 1) {
                        currentQuestionIndex.value++

                    }


                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(10.dp),
            ) {
                Text(text = stringResource(R.string.next_questions))
            }
        } else if (!AnsrCorrect && showFeedback) {
            showFeedback = true
            AnswerFeedback(stringResource(R.string.wrong), colorScheme.error, true)

        }

        if (showAnswerOptionsRow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {

                TrueFalseButton("True") {
                    AnsrCorrect = true == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
                TrueFalseButton("False") {
                    AnsrCorrect = false == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
            }
        }

        if (showResetButton) {
            showAnswerOptionsRow = false
            Button(onClick = {
                showAnswerOptionsRow = true
                showResetButton = false

                currentQuestionIndex.value = 0
                userScore.value = 0
                showAnswerOptionsRow = true


            }) {
                Text(text = stringResource(R.string.reset_games))
            }
        }
    }
}

fun stringResource(id: String): String {
    TODO("Not yet implemented")
}

@Composable
fun TrueFalseButton(text:  String, showAnswerOptionsRow: () -> Unit) {
    Button(
        onClick = {
            showAnswerOptionsRow()

        },
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)

    ) {
        Text(text = stringResource(R.string.text))
    }
}

@Composable
fun AnswerFeedback(message: String, backgroundColor: Color, isCorrect: Boolean) {
    Box(
        modifier = Modifier

            .size(100.dp)
            .clip(CircleShape)
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .padding(16.dp),
        Alignment.Center

    ) {

        val imagePainter = if (isCorrect) {
            painterResource(id = R.drawable.correct_answer)
        } else {
            painterResource(id = R.drawable.wrong_answer)
        }

        Image(
            painter = imagePainter,
            contentDescription = if (isCorrect) "Correct Answer Image" else "Wrong Answer Image",
            modifier = Modifier.fillMaxSize()
        )


        Text(
            text = message,
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onPrimary

        )
    }
}


@Preview(showBackground = true)
@Composable
fun TrueFalseGamePreview() {
    TrueFalseGame()
}