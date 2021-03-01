import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Formatter from './formatter'

Vue.use(VueI18n)

const locale = 'en-US' // default locale
const formatter = new Formatter({ locale })

const helloIndirectIndirect = {
  short: 'hello!!',
  full: 'hey there!!!'
};

const helloIndirect = helloIndirectIndirect;

const hello = helloIndirect;

export default new VueI18n({
  locale,
  formatter,
  messages: {
    'en-US': {
      tos: 'Terms of service',
      terms: 'I accept the {0}',
      message: {
        hello: hello,
        plural: 'You have {n, plural, =0{no messages} one{1 message} other{# messages}}.',
        select: '{gender, select, male{He} female{She} other{They}} liked this.',
        number: 'Current Percent: {current, number, percent}',
        time: 'Current Time: {current, time, short}',
      },
      fly: {
        departure: 'You can {action} until {limit} minutes from departure',
        board: 'board'
      }
    }
  }
})
