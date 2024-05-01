// Pinia store
import { defineStore } from 'pinia'

// Api
import axios from '@src/apis/axios'

// Interface
import { IContact, IContactGroup, IAccountValues } from '@src/interface/userdata'
import { Ref, ref } from 'vue';

const currentPage: Ref<number> = ref(1);

const hasNext: Ref<boolean> = ref(false);

const hasNextContactAll: Ref<boolean> = ref(false);

const currentPageContactAll: Ref<number> = ref(1);

export const useUserStore = defineStore('user', {

  state: () => {
    return {      
      currentPage: currentPage,
      hasNextContacted: hasNext,
      hasNextContactAll: hasNextContactAll,
      contactGroups: [],
      loadingSession: false,
      contacts: [] as IContact[],
      contactNotFriend: [] as IContact[],
      allContactInSystem: [] as IContact[],
      currentContact: {} as IContact
    }
  },
  getters: {
    // getAccessToken: (state): string | undefined => state.user.token,
    contactByGroups: (state) => {
      state.contacts.sort()
      let groups: IContactGroup[] = [];
      let currentLetter: string = "";
      let groupNames: string[] = [];

      for (let contact of state.contacts) {
        // console.log(contact)
        // if the first letter is different create a new group.
        if (contact.firstName[0].toUpperCase() !== currentLetter) {
          currentLetter = contact.firstName[0].toUpperCase();
          groupNames.push(currentLetter);
        }
        groupNames.sort();
      }

      for (let groupName of groupNames) {
        let group: IContactGroup = { letter: groupName, contacts: [] };
        for (let contact of state.contacts) {
          if (contact.firstName[0].toUpperCase() === groupName) {
            group.contacts.push(contact);
          }
        }
        groups.push(group);
      }
      return groups
    },
    getContactsOfUser: (state) => {
      return state.contacts.sort();
    },
    currentContactByEmail: (state) => {
      return state.currentContact;
    },
    getContactNotFriends: (state) => {
      return state.contactNotFriend;
    },
    getAllContactInSystem: (state) => {
      return state.allContactInSystem;
    },
    getHasNextContactAll: (state) => {
      return state.hasNextContactAll;
    }
  },
  actions: {
    async getContacts(): Promise<void> {
      const email = localStorage.getItem("email");
      try {
        const { data } = await axios.get('/chat/contacts/' + email)
        this.contacts = data
      } catch (error: unknown) {
        console.log('error')
      } finally {
        this.loadingSession = false
      }
    },
    async getCurrentContact(): Promise<void> {
      try {
        const { data } = await axios.get('/chat/current-contact?email=' + localStorage.getItem('email'))
        this.currentContact = data
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async addContact(addEmail: String): Promise<void> {
      const currentEmail = localStorage.getItem('email');
      const payload = { addEmail: addEmail, currentEmail: currentEmail };
      const { data } = await axios.post('/chat/add-friend', payload);
    },
    async removeContact(senderId: number, receiverId: number): Promise<void> {
      await axios.post('/chat/remove-contact?senderId=' + senderId + "&receiverId=" + receiverId);
      const contactFilter = this.contacts.filter(contact => contact.id != receiverId)
      this.contacts = [];
      this.contacts = contactFilter;
    },
    async updateUserInfo(accountValues: IAccountValues, file : File | undefined): Promise<void> {
      const { id: id, email: email, firstName: firstName, lastName: lastName, avatar: avatar } = accountValues
      var bodyFormData = new FormData();
      if(file){
        bodyFormData.append("file", file);
      }
      bodyFormData.append("contactString", JSON.stringify({ id, email, firstName, lastName, avatar }))
      await axios.put('/chat/contact-info', bodyFormData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
    },
    async contactAction(status: number, addContact: string): Promise<void> {
      const currentEmail = localStorage.getItem('email');
      await axios.post('/chat/friend-action/' + currentEmail + "?addContact=" + addContact + "&status=" + status);
    },
    async getAllContactInSystemNotFriend(page: number, contactId: number): Promise<void> {
      const {data} = await axios.get('/chat/contact-not-friend/' + contactId + "?page=" + page + "&size=" + 10);
      this.contactNotFriend = data.content;
      this.currentPage = page;
      this.hasNextContacted = data.hasNext;
    },
    updateOnlineStatus(email: String, status: boolean): void {
      const index = this.contacts.findIndex(con => con.email == email);
      this.contacts[index].online = status;
    },
    async getAllContactInSystemApi(page: number): Promise<void> {
      const currentEmail = localStorage.getItem('email');
      const {data} = await axios.get('/chat/contact-all/'+ currentEmail + "?page=" + page + "&size=" + 10);
      this.allContactInSystem = data.content;
      this.currentPage = page;
      this.hasNextContactAll = data.hasNext;
    }
  }
})
